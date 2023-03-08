package com.yhz.senbeiforummain.model.dto.topicreplysupport;

import lombok.Data;

/**
 * @author yanhuanzhan
 * @date 2023/3/7 - 18:17
 */
@Data
public class TopicReplySupportAddRequest {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 一级回复id
     */
    private Long topicReplyId;

    /**
     * 是否点赞(0-未点赞，1-已点赞)
     */
    private Integer isSupport;
}
