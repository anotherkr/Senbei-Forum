package com.yhz.senbeiforummain.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.senbeiforummain.model.dto.topic.TopicQueryRequest;
import com.yhz.senbeiforummain.model.entity.Module;
import com.yhz.senbeiforummain.model.entity.Topic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhz.senbeiforummain.model.to.ModuleConcernTopicTo;
import com.yhz.senbeiforummain.model.to.TopicTo;
import com.yhz.senbeiforummain.model.vo.ModuleConcernTopicVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author 吉良吉影
* @description 针对表【topic(主贴表)】的数据库操作Mapper
* @createDate 2022-11-13 16:08:53
* @Entity com.yhz.senbeiforummain.domain.Topic
*/
@Repository

public interface TopicMapper extends BaseMapper<Topic> {

    /**
     * 根据模块id集合查询关注模块对应主贴集合并分页
     * @param topicIPage
     * @param moduleIdList
     * @param sortField
     * @param sortOrder
     * @return
     */
    IPage<ModuleConcernTopicTo> selectModuleConcernToPage(IPage<Topic> topicIPage, @Param("moduleIdList") List<Long> moduleIdList, @Param("sortField") String sortField, @Param("sortOrder")String sortOrder);

    IPage<TopicTo> selectTopicToPage(IPage<Topic> page,@Param("topicQueryRequest") TopicQueryRequest topicQueryRequest);

    /**
     * 根据模块id集合查询模块的帖子数
     * @param moduleIdList
     * @return
     */
    List<Module> getTopicNumByModuleIdList(@Param("moduleIdList") List<Long> moduleIdList);
}




