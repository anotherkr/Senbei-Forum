package com.yhz.senbeiforummain.mapper;

import com.yhz.senbeiforummain.domain.ModuleTopic;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhz.senbeiforummain.domain.vo.TopicDetailVo;
import org.springframework.stereotype.Repository;

/**
* @author 吉良吉影
* @description 针对表【module_topic(主贴表)】的数据库操作Mapper
* @createDate 2022-11-13 16:08:53
* @Entity com.yhz.senbeiforummain.domain.ModuleTopic
*/
@Repository
public interface ModuleTopicMapper extends BaseMapper<ModuleTopic> {


}




