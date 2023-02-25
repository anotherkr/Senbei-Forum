package com.yhz.senbeiforummain.security.constant;

import com.yhz.commonutil.common.RedisBasePrefixKey;

/**
 * @author 吉良吉影
 */
public class RedisRememberMeKey extends RedisBasePrefixKey {

    public static final String SERIES_KEY = "rememberMe--series";
    public static final String USERNAME_KEY = "rememberMe--username";

    /**
     * 前缀+series
     */
    public static RedisRememberMeKey getSeriesKey = new RedisRememberMeKey(SERIES_KEY,3600*24L*14);
    public static RedisRememberMeKey getUsernameKey = new RedisRememberMeKey(USERNAME_KEY,3600*24L*14);


    private RedisRememberMeKey(String prefix, Long expireSeconds) {
        super(prefix,expireSeconds);
    }

    public RedisRememberMeKey(String prefix) {
        super(prefix);
    }
}