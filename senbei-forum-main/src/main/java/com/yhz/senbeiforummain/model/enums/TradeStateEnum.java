package com.yhz.senbeiforummain.model.enums;

public enum TradeStateEnum {
    /**
     * 支付成功
     */
    SUCCESS(0),

    /**
     * 转入退款
     */
    REFUND_PROCRESSING(1),

    /**
     * 未支付
     */
    NOTPAY(2),

    /**
     * 已关闭
     */
    CLOSED(3),

    /**
     * 支付失败
     */
    PAYERROR(4),
    /**
     * 退款成功
     */
    REFUND_SUCCESS(5),
    /**
     * 退款失败
     */
    REFUND_FAIL(6)
    ;

    private Integer code;

    TradeStateEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
