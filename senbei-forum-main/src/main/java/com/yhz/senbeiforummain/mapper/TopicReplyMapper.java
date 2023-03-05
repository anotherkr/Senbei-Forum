package com.yhz.senbeiforummain.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.senbeiforummain.model.entity.TopicReply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhz.senbeiforummain.model.to.TopicReplyTo;
import com.yhz.senbeiforummain.model.vo.TopicReplyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 吉良吉影
* @description 针对表【topic_reply(主贴回复表)】的数据库操作Mapper
* @createDate 2022-11-13 16:08:53
* @Entity com.yhz.senbeiforummain.domain.TopicReply
*/
public interface TopicReplyMapper extends BaseMapper<TopicReply> {
    /**
     * 根据主贴id查询回复表集合
     * @param topicId
     * @return
     */
    IPage<TopicReplyTo> selectTopicReplyVoIPageByTopicId(@Param("iPage") IPage<TopicReplyTo> iPage, @Param("topicId")Long topicId, @Param("sortField") String sortField, @Param("sortOrder")String sortOrder);
}




