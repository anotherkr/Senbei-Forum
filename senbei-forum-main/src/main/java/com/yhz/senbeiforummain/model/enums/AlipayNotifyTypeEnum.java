package com.yhz.senbeiforummain.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 回调接口路径
 * @author 吉良吉影
 */

@AllArgsConstructor
@Getter
public enum AlipayNotifyTypeEnum {
    /**
     * 支付通知
     */
    PC_NOTIFY("/pay/alipay/pay-notify");

    ///**
    // * 退款结果通知
    // */
    //REFUND_NOTIFY("/api/wechat-pay/refunds/notify");

    /**
     * 类型
     */
    private final String type;
}