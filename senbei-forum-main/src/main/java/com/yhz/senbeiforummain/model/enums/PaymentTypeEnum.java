package com.yhz.senbeiforummain.model.enums;

/**
 * 支付类型枚举类
 * @author yanhuanzhan
 * @date 2022/12/14 - 23:35
 */
public enum PaymentTypeEnum {
    ALI_PAY(1, "ali_pay","支付宝支付"),
    WECHAT_PAY(0,"wechat_pay", "微信支付");

    PaymentTypeEnum(int code, String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static PaymentTypeEnum getByCode(int code) {
        for (PaymentTypeEnum paymentTypeEnum : PaymentTypeEnum.values()) {
            if (paymentTypeEnum.code == code) {
                return paymentTypeEnum;
            }
        }
        return null;
    }
    private int code;
    private String value;
    private String desc;

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getValue() {
        return value;
    }
}
