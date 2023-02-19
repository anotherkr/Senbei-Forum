package com.yhz.senbeiforummain.constant;

import lombok.Data;

/**
 * @author 吉良吉影
 */
@Data
public abstract class RedisBasePrefixKey implements RedisPrefixKey{
    /**
     * redis key前缀
     */
    private String prefix;

    /**
     * 过期时间
     */
    private Long expireSeconds;

    /**
     * 构造器
     * expireSeconds 为零默认为永不过期
     *
     * @param prefix 前缀
     */
    public RedisBasePrefixKey(String prefix) {
        this.prefix = prefix;
        this.expireSeconds = 0L;
    }

    /**
     * 构造器
     * @param expireSeconds 过期时间
     * @param prefix 前缀
     */
    public RedisBasePrefixKey(String prefix,Long expireSeconds){
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }
  
    /**
     * 获取过期时间
     * @return
     */
    @Override
    public Long expireSeconds() {
        return expireSeconds;
    }

    /**
     * 获取Key前缀
     * @return
     */
    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":"+prefix+":";
    }
}