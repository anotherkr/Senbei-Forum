package com.yhz.senbeiforummain.security.enums;










/**
 * @author 吉良吉影
 */

public enum SecurityErrorCode {

    SUCCESS(200,"请求成功!",""),
    ERROR(500,"服务器响应错误！",""),

    /** 10XX 表示用户错误*/
    USER_REGISTER_FAILED(1001, "注册失败",""),
    USER_ACCOUNT_EXISTED(1002,"用户名已存在",""),
    USER_ACCOUNT_NOT_EXIST(1003,"用户名不存在",""),
    USERNAME_PASSWORD_ERROR(1004,"用户名或密码错误",""),
    PASSWORD_ERROR(1005,"密码错误",""),
    USER_ACCOUNT_EXPIRED(1006,"账号过期",""),
    USER_PASSWORD_EXPIRED(1007,"密码过期",""),
    USER_ACCOUNT_DISABLE(1008,"账号不可用",""),
    USER_ACCOUNT_LOCKED(1009,"账号锁定",""),
    USER_NOT_LOGIN(1010,"用户未登陆",""),
    USER_NO_PERMISSIONS(1011,"用户权限不足",""),
    USER_SESSION_INVALID(1012,"会话已超时",""),
    USER_ACCOUNT_LOGIN_IN_OTHER_PLACE(1013,"账号超时或账号在另一个地方登陆",""),
    TOKEN_VALIDATE_FAILED(1014,"Token令牌验证失败",""),



    /** 20XX 表示服务器错误 */
    UPDATE_USER_INFO_FAILED(2004,"修改用户信息失败",""),
    UPDATE_USER_PASSWORD_FAILED(2005,"修改密码失败",""),
    ;

    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述（详情）
     */
    private final String description;

    SecurityErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
