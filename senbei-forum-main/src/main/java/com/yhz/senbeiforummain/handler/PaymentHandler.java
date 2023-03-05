package com.yhz.senbeiforummain.handler;

import com.alipay.api.response.AlipayTradeQueryResponse;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.model.enums.PaymentTypeEnum;

/**
 * 支付功能处理器接口
 * @author yanhuanzhan
 * @date 2022/12/14 - 23:34
 */
public interface PaymentHandler {
    /**
     * 获取支付渠道
     * @return
     */
    PaymentTypeEnum getChannel();

    /**
     * 调用支付下单接口
     * @param orderNo
     * @param productName
     * @param notifyUrl
     * @param totalFee
     * @return
     * @throws BusinessException
     */
    String pcPreOrder(String orderNo, String productName, String notifyUrl, Integer totalFee) throws BusinessException;


    /**
     * 调用关单接口
     * @param orderNo
     * @return
     */
    void closeOrder(String orderNo);
    /**
     * 调用支付宝查询订单接口
     * @return
     */
    AlipayTradeQueryResponse queryOrder(String orderNo);
}
