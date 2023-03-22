package com.yhz.senbeiforummain.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.senbeiforummain.config.pay.AlipayConfig;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.model.dto.alipay.AlipayPayNotify;
import com.yhz.senbeiforummain.model.entity.OrderInfo;
import com.yhz.senbeiforummain.model.enums.AlipayTradeStatus;
import com.yhz.senbeiforummain.model.enums.PaymentTypeEnum;
import com.yhz.senbeiforummain.model.enums.TradeStateEnum;
import com.yhz.senbeiforummain.service.IOrderInfoService;
import com.yhz.senbeiforummain.mapper.OrderInfoMapper;
import com.yhz.senbeiforummain.service.IPaymentInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author 吉良吉影
 * @description 针对表【order_info】的数据库操作Service实现
 * @createDate 2023-03-03 14:22:23
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
        implements IOrderInfoService {
    @Resource
    private AlipayConfig alipayConfig;
    @Resource
    private IPaymentInfoService paymentInfoService;
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
        int totalAmountInt = totalAmount.multiply(new BigDecimal(100)).intValue();
        int totalFeeInt = order.getTotalFee();
        if (totalAmountInt != totalFeeInt) {
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
        //处理支付成功的情况
        if (AlipayTradeStatus.TRADE_SUCCESS.equals(payNotify.getTradeStatus()) || AlipayTradeStatus.TRADE_FINISHED.equals(payNotify.getTradeStatus())) {
            orderInfo.setOrderStatus(TradeStateEnum.SUCCESS.getCode());
        } else if (AlipayTradeStatus.TRADE_CLOSED.equals(payNotify.getTradeStatus())) {
            orderInfo.setOrderStatus(TradeStateEnum.CLOSED.getCode());
        }
        //更新订单状态
        this.updateById(orderInfo);
        //记录支付日志
        String decryptData = JSONObject.toJSONString(payNotify);
        paymentInfoService.createPaymentInfo(decryptData, PaymentTypeEnum.ALI_PAY);
    }
}




