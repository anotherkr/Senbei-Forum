package com.yhz.senbeiforummain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayConstants;
import com.alipay.api.internal.util.AlipaySignature;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.senbeiforummain.config.pay.AlipayConfig;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.factory.PaymentFactory;
import com.yhz.senbeiforummain.handler.PaymentHandler;
import com.yhz.senbeiforummain.mapper.OrderInfoMapper;
import com.yhz.senbeiforummain.mapper.ProductMapper;
import com.yhz.senbeiforummain.model.dto.alipay.AlipayPayNotify;
import com.yhz.senbeiforummain.model.entity.Product;
import com.yhz.senbeiforummain.model.enums.AlipayNotifyTypeEnum;
import com.yhz.senbeiforummain.model.enums.PaymentTypeEnum;
import com.yhz.senbeiforummain.service.IPayService;
import com.yhz.senbeiforummain.service.OrderInfoService;
import com.yhz.senbeiforummain.util.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * @author yanhuanzhan
 * @date 2023/3/3 - 16:55
 */
@Service
@Slf4j
public class PayServiceImpl implements IPayService {
    @Resource
    private OrderInfoService orderInfoService;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private PaymentFactory paymentFactory;
    @Resource
    private AlipayConfig alipayConfig;
    @Override
    public String preOrder(Long userId, Long productId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        Product product = Optional.ofNullable(productMapper.selectById(productId))
                .orElseThrow(()->new BusinessException(ErrorCode.NULL_ERROR));
        //商品库存减1
        product.setStock(product.getStock() - 1);
        productMapper.updateById(product);
        BigDecimal price = product.getPriceDiscount();
        if (price == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        //分转元
        Integer newPrice = price.multiply(new BigDecimal(100)).intValue();
        //创建订单编号
        String orderNo = OrderNoUtils.getOrderNo();
        PaymentHandler handler = paymentFactory.getHandler(PaymentTypeEnum.ALI_PAY);
        String formStr = handler.pcPreOrder(orderNo, product.getName(), alipayConfig.getNotifyDomain()
                .concat(AlipayNotifyTypeEnum.PC_NOTIFY.getType()), newPrice);
        int save=orderInfoMapper.createOrder(userId,orderNo, product, PaymentTypeEnum.ALI_PAY.getCode());
        if (save > 0) {
            return formStr;
        }else{
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }

    }

    @Override
    public String aliPayAcceptNotify(Map<String, String> params) {
        String result = null;
        try {
            result = "failure";
            AlipayPayNotify payNotify = JSONObject.parseObject(JSONObject.toJSONString(params), AlipayPayNotify.class);
            log.info("paynotify params:{}", payNotify);
            //异步通知验签
            boolean signVerified = AlipaySignature.rsaCheckV1(
                    params,
                    alipayConfig.getAlipayPublicKey(),
                    AlipayConstants.CHARSET_UTF8,
                    AlipayConstants.SIGN_TYPE_RSA2);

            if (!signVerified) {
                //验签失败则记录异常日志，并在response中返回failure.
                log.error("支付结果异步通知验签失败！");
                return result;
            }
            // 验签成功后
            log.info("支付成功异步通知验签成功！");
            //按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，
            Boolean checkResult = orderInfoService.checkAlipayPayNotify(payNotify);
            if (!checkResult) {
                result = "failure";
                return result;
            }
            //处理业务 修改订单状态 记录支付日志
            orderInfoService.processAlipayPayNotify(payNotify);
        } catch (AlipayApiException e) {
            result = "failure";
            return result;
        }
        result = "success";
        return result;
    }

}
