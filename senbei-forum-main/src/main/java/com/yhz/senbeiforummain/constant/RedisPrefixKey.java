package com.yhz.senbeiforummain.constant;

/**
 * Redis前缀key接口
 * @author 吉良吉影
 */
public interface RedisPrefixKey {
    /**
     * redis 过期时间
     * @return 过期时间
     */
    Long expireSeconds();

    /**
     * redis key
     * @return 键前缀
     */
    String getPrefix();
}