package com.yhz.senbeiforummain.constant.rediskey;

import com.yhz.senbeiforummain.constant.RedisBasePrefixKey;

/**
 * 用户redis前缀
 * @author 吉良吉影
 */
public class RedisUserKey extends RedisBasePrefixKey {

    public static final String USER_KEY_TOKEN = "token";
    public static final String USER_KEY_INFO = "info";
    public static final String USER_KEY_EMAIL_CODE = "email_code";
    /**
     * 前缀+用户名
     */
    public static RedisUserKey getUserToken = new RedisUserKey(USER_KEY_TOKEN,3600*24L);
    public static RedisUserKey getUserInfo = new RedisUserKey(USER_KEY_INFO,3600*24L);
    public static RedisUserKey getUserEmailCode = new RedisUserKey(USER_KEY_EMAIL_CODE,300L);

    private RedisUserKey(String prefix) {
        super(prefix);
    }

    private RedisUserKey(String prefix,Long expireSeconds) {
        super(prefix,expireSeconds);
    }
}