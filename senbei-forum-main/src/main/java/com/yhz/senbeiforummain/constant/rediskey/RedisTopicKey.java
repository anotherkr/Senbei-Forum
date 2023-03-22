package com.yhz.senbeiforummain.constant.rediskey;

import com.yhz.commonutil.common.RedisBasePrefixKey;

/**
 * 主贴redis前缀
 * @author 吉良吉影
 */
public class RedisTopicKey extends RedisBasePrefixKey {
    /**
     * 点赞缓存前缀(缓存一级回复 是否点赞,采用hash，key采用 回复id::用户id 的形式
     */
    public static final String TOPIC_SUPPORT_INFO = "topic_support_info";
    /**
     * 缓存点赞数
     */
    public static final String TOPIC_SUPPORT_COUNT = "topic_support_count";
    /**
     * 缓存访问数
     */
    public static final String TOPIC_CLICK_NUM = "topic_click_num";
    /**
     * 仅前缀
     */
    public static RedisTopicKey getSupportInfo = new RedisTopicKey(TOPIC_SUPPORT_INFO,24*60*60L);
    /**
     * 前缀+topicId
     */
    public static RedisTopicKey getSupportCount = new RedisTopicKey(TOPIC_SUPPORT_COUNT,24*60*60L);
    /**
     * 仅前缀(hash:topicId,clickNum)
     */
    public static RedisTopicKey getClickNum = new RedisTopicKey(TOPIC_CLICK_NUM,24*60*60L);

    @Override
    public String getPrefix() {
        return super.getPrefix();
    }


    private RedisTopicKey(String prefix, Long expireSeconds) {
        super(prefix,expireSeconds);
    }
}