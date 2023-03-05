package com.yhz.senbeiforummain.service;

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


    String aliPayAcceptNotify(Map<String, String> params);
}
