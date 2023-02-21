package com.yhz.senbeiforummain.mapper;

import com.yhz.senbeiforummain.model.entity.Module;
import com.yhz.senbeiforummain.model.entity.ModuleConcern;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 吉良吉影
* @description 针对表【module_concern(模块关注表)】的数据库操作Mapper
* @createDate 2023-02-20 13:09:54
* @Entity com.yhz.senbeiforummain.model.entity.ModuleConcern
*/
public interface ModuleConcernMapper extends BaseMapper<ModuleConcern> {
    /**
     * 根据模块id集合获取模块关注数
     * @param moduleIdList
     * @return
     */
    List<Module> getConcernNumByModuleIdList(@Param("moduleIdList") List<Long> moduleIdList);
}




