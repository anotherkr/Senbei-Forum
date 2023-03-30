package com.yhz.senbeiforummain.model.enums;

/**
 * 登录渠道枚举类
 * @author yanhuanzhan
 * @date 2022/12/14 - 23:35
 */
public enum LoginChannelEnum {
    GITEE_LOGIN(1, "gitee","Gitee登录"),
    GITHUB_LOGIN(0,"github", "GitHub登录"),
    QQ_LOGIN(1, "qq","qq登录"),
    ;

    LoginChannelEnum(int code,String value, String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }

    public static LoginChannelEnum getByCode(int code) {
        for (LoginChannelEnum loginChannelEnum : LoginChannelEnum.values()) {
            if (loginChannelEnum.code == code) {
                return loginChannelEnum;
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
