package com.yhz.senbeiforummain.service;

import com.yhz.senbeiforummain.model.entity.TopicReply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhz.senbeiforummain.model.dto.topicreply.TopicReplyAddRequst;

import javax.servlet.http.HttpServletRequest;

/**
* @author 吉良吉影
* @description 针对表【topic_reply(主贴回复表)】的数据库操作Service
* @createDate 2022-11-13 16:08:53
*/
public interface ITopicReplyService extends IService<TopicReply> {

    void reply(TopicReplyAddRequst topicReplyAddRequst, Long userId, HttpServletRequest request);

    /**
     * 点赞功能
     * @param topicReplyId
     * @param userId
     */
    void support(Long topicReplyId, Long userId);
}
