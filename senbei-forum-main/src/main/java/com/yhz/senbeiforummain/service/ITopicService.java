package com.yhz.senbeiforummain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.senbeiforummain.model.entity.Topic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhz.senbeiforummain.model.dto.topic.TopicQueryRequest;
import com.yhz.senbeiforummain.model.dto.topic.TopicAddRequst;
import com.yhz.senbeiforummain.model.vo.TopicVo;
import com.yhz.senbeiforummain.model.vo.TopicDetailVo;
import com.yhz.senbeiforummain.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;

/**
* @author 吉良吉影
* @description 针对表【topic(主贴表)】的数据库操作Service
* @createDate 2022-11-13 16:08:53
*/
public interface ITopicService extends IService<Topic> {
    /**
     * 分页查询
     * @param topicQueryRequest 查询条件
     * @return
     */
    IPage<TopicVo> pageList(TopicQueryRequest topicQueryRequest) throws BusinessException;

    /**
     * 发布主贴
     * @param topicAddRequst
     * @param userId
     * @param request
     */
    void publish(TopicAddRequst topicAddRequst, Long userId, HttpServletRequest request);

    /**
     * 获取主贴详细信息
     * @param topicId
     * @return
     */
    TopicDetailVo getTopicDetailVo(Long topicId);


}
