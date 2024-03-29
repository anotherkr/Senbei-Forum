package com.yhz.senbeiforummain.constant.rediskey;

import com.yhz.commonutil.common.RedisBasePrefixKey;

/**
 * 一级回复redis前缀
 * @author 吉良吉影
 */
public class RedisTopicReplyKey extends RedisBasePrefixKey {
    /**
     * 点赞缓存前缀(缓存一级回复 是否点赞,采用hash，key采用 回复id::用户id 的形式
     */
    public static final String TOPIC_REPLY_SUPPORT_INFO = "topic_reply_support_info";
    /**
     * 缓存点赞数
     */
    public static final String TOPIC_REPLY_SUPPORT_COUNT = "topic_reply_support_count";
    /**
     * 仅前缀
     */
    public static RedisTopicReplyKey getSupportInfo = new RedisTopicReplyKey(TOPIC_REPLY_SUPPORT_INFO,24*60*60L);
    /**
     * 前缀+topicReplyId
     */
    public static RedisTopicReplyKey getSupportCount = new RedisTopicReplyKey(TOPIC_REPLY_SUPPORT_COUNT,24*60*60L);

    @Override
    public String getPrefix() {
        return super.getPrefix();
    }


    private RedisTopicReplyKey(String prefix, Long expireSeconds) {
        super(prefix,expireSeconds);
    }
}