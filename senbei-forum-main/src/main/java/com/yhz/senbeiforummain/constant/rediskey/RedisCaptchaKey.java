package com.yhz.senbeiforummain.constant.rediskey;

import com.yhz.senbeiforummain.constant.RedisBasePrefixKey;

/**
 * @author yanhuanzhan
 * @date 2023/2/21 - 20:58
 */
public class RedisCaptchaKey extends RedisBasePrefixKey {
    public static final String Captcha = "captcha";

    /**
     * 前缀+uuid
     */
    public static RedisCaptchaKey getCaptchaCode = new RedisCaptchaKey(Captcha,300L);

    private RedisCaptchaKey(String prefix) {
        super(prefix);
    }

    private RedisCaptchaKey(String prefix,Long expireSeconds) {
        super(prefix,expireSeconds);
    }
}
