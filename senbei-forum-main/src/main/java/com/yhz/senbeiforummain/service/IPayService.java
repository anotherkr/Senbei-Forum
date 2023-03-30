package com.yhz.senbeiforummain.service;

import com.alipay.api.AlipayApiException;

import java.util.Map;

/**
 * @author yanhuanzhan
 * @date 2023/3/3 - 16:55
 */
public interface IPayService {
    /**
     * 支付下单
     *
     * @param userId
     * @param productId
     * @return
     */
    String preOrder(Long userId, Long productId);

    /**
     * 接收支付宝支付通知
     * @param params
     * @return
     */
    String aliPayAcceptNotify(Map<String, String> params);

    /**
     * 调用支付宝关单接口
     * @param orderNo
     * @throws AlipayApiException
     */
    void closeOrder(String orderNo) throws AlipayApiException;

}
