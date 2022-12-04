package com.yhz.senbeiforummain.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yhz.senbeiforummain.domain.ModuleTopicReply;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhz.senbeiforummain.domain.vo.TopicReplyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 吉良吉影
* @description 针对表【module_topic_reply(主贴回复表)】的数据库操作Mapper
* @createDate 2022-11-13 16:08:53
* @Entity com.yhz.senbeiforummain.domain.ModuleTopicReply
*/
public interface ModuleTopicReplyMapper extends BaseMapper<ModuleTopicReply> {
    /**
     * 根据主贴id查询回复表集合
     * @param topicId
     * @return
     */
    List<TopicReplyVo> selectTopicReplyVoListByTopicId(@Param("topicId")Long topicId);
}




