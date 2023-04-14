package com.yhz.senbeiforummain.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.common.PageRequest;
import com.yhz.senbeiforummain.config.pay.AlipayConfig;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.mapper.ProductMapper;
import com.yhz.senbeiforummain.model.dto.alipay.AlipayPayNotify;
import com.yhz.senbeiforummain.model.entity.OrderInfo;
import com.yhz.senbeiforummain.model.entity.Product;
import com.yhz.senbeiforummain.model.enums.AlipayTradeStatus;
import com.yhz.senbeiforummain.model.enums.PaymentTypeEnum;
import com.yhz.senbeiforummain.model.enums.QueueEnum;
import com.yhz.senbeiforummain.model.enums.TradeStateEnum;
import com.yhz.senbeiforummain.model.vo.OrderInfoVo;
import com.yhz.senbeiforummain.service.IOrderInfoService;
import com.yhz.senbeiforummain.mapper.OrderInfoMapper;
import com.yhz.senbeiforummain.service.IPaymentInfoService;
import com.yhz.senbeiforummain.service.IProductService;
import com.yhz.senbeiforummain.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 吉良吉影
 * @description 针对表【order_info】的数据库操作Service实现
 * @createDate 2023-03-03 14:22:23
 */
@Service
@Slf4j
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
        implements IOrderInfoService {
    @Resource
    private AlipayConfig alipayConfig;
    @Resource
    private IPaymentInfoService paymentInfoService;
    @Resource
    private IProductService productService;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Override
    public Boolean checkAlipayPayNotify(AlipayPayNotify payNotify) {
        //1 商户需要验证该通知数据中的 out_trade_no 是否为商户系统中创建的订单号
        String outTradeNo = payNotify.getOutTradeNo();
        OrderInfo order = this.getByOrderNo(outTradeNo);
        if (ObjectUtils.isEmpty(order)) {
            log.error("订单不存在");
            return false;
        }
        //2 判断 total_amount 是否确实为该订单的实际金额（即商户订单创建时的金额）
        BigDecimal totalAmount = payNotify.getTotalAmount();
        BigDecimal orderTotalFee = order.getTotalFee();
        if (totalAmount != null && totalAmount.equals(orderTotalFee)) {
            log.error("金额校验失败");
            return false;
        }
        //3 校验通知中的 seller_id（或者 seller_email) 是否为 out_trade_no 这笔单据的对应的操作方
        if (!alipayConfig.getSellerId().equals(payNotify.getSellerId())) {
            log.error("商家pid校验失败");
            return false;
        }

        //4 验证 app_id 是否为该商户本身
        if (!alipayConfig.getAppid().equals(payNotify.getAppId())) {
            log.error("appid校验失败");
            return false;
        }

        //在支付宝的业务通知中，只有交易通知状态为 TRADE_SUCCESS时，
        // 支付宝才会认定为买家付款成功。
        if (!AlipayTradeStatus.TRADE_SUCCESS.getType().equals(payNotify.getTradeStatus())) {
            log.error("支付失败");
            return false;
        }
        return true;
    }

    @Override
    public OrderInfo getByOrderNo(String outTradeNo) {
        if (StrUtil.isEmpty(outTradeNo)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no", outTradeNo);
        return this.getOne(wrapper);

    }

    @Override
    public void processAlipayPayNotify(AlipayPayNotify payNotify) {
        //处理重复通知
        OrderInfo orderInfo = this.getByOrderNo(payNotify.getOutTradeNo());
        if (ObjectUtils.isEmpty(orderInfo)) {
            throw new RuntimeException("订单不存在");
        }
        if (TradeStateEnum.SUCCESS.getCode().equals(orderInfo.getOrderStatus())) {
            return;
        }
        log.info("receive pay notify,tradeStatus:{}", payNotify.getTradeStatus());
        //处理支付成功的情况
        if (AlipayTradeStatus.TRADE_SUCCESS.getType().equals(payNotify.getTradeStatus()) || AlipayTradeStatus.TRADE_FINISHED.getType().equals(payNotify.getTradeStatus())) {
            orderInfo.setOrderStatus(TradeStateEnum.SUCCESS.getCode());
        } else if (AlipayTradeStatus.TRADE_CLOSED.getType().equals(payNotify.getTradeStatus())) {
            orderInfo.setOrderStatus(TradeStateEnum.CLOSED.getCode());
        }
        //更新订单状态
        this.updateById(orderInfo);
        //记录支付日志
        String decryptData = JSONObject.toJSONString(payNotify);
        paymentInfoService.createPaymentInfo(decryptData, PaymentTypeEnum.ALI_PAY);
    }

    @Override
    public IPage<OrderInfoVo> getOrderInfoVoByPage(PageRequest pageRequest, Long userId) {
        Long pageSize = pageRequest.getPageSize();
        Long current = pageRequest.getCurrent();
        String sortOrder = pageRequest.getSortOrder();
        String sortField = pageRequest.getSortField();
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        QueryWrapper<OrderInfo> queryWrapper = new QueryWrapper<>();
        PageUtil.dealSortWrapper(queryWrapper, sortField, sortOrder);
        IPage<OrderInfo> iPage = PageUtil.vaildPageParam(current, pageSize);
        queryWrapper.eq("user_id", userId);
        IPage<OrderInfo> orderInfoIPage = baseMapper.selectPage(iPage, queryWrapper);
        IPage<OrderInfoVo> orderInfoVoIPage = orderInfoIPage.convert(item -> {
            OrderInfoVo orderInfoVo = new OrderInfoVo();
            BeanUtils.copyProperties(item, orderInfoVo);
            return orderInfoVo;
        });
        List<Long> productIdList = orderInfoIPage.getRecords().stream().map(item -> item.getProductId()).collect(Collectors.toList());
        List<Product> products = productService.listByIds(productIdList);
        HashMap<Long, String> productNameMap = new HashMap<>();
        products.forEach(item -> {
            productNameMap.put(item.getId(), item.getName());
        });
        orderInfoVoIPage.getRecords().forEach(item -> {
            Long productId = item.getProductId();
            if (productId != null) {
                item.setProductName(productNameMap.get(productId));
            }
        });
        return orderInfoVoIPage;
    }

    @Override
    public String getOrderNo(Long productId, Long userId, PaymentTypeEnum paymentTypeEnum) {
        String orderNo = UUID.randomUUID().toString().substring(20);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setUserId(userId);
        orderInfo.setOrderNo(orderNo);
        orderInfo.setOrderStatus(TradeStateEnum.NOTPAY.getCode());
        Product product = Optional.ofNullable(productService.getById(productId)).orElseThrow(() -> new BusinessException(ErrorCode.NULL_ERROR));
        orderInfo.setTotalFee(product.getPriceDiscount());
        orderInfo.setPaymentType(paymentTypeEnum.getCode());
        rabbitTemplate.convertAndSend(QueueEnum.QUEUE_ORDER_CREATE.getExchange(),QueueEnum.QUEUE_ORDER_CREATE.getRouteKey(),orderInfo);
        return orderNo;
    }

    @Override
    public void createOrder(OrderInfo orderInfo) {
        Long productId = orderInfo.getProductId();
        Product product = productService.getById(productId);
        product.setStock(product.getStock()-1);
        productService.updateById(product);
        boolean save = this.save(orderInfo);
        if (!save) {
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
    }
}




