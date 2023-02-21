package com.yhz.senbeiforummain.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.annotation.SqlParser;
import com.yhz.senbeiforummain.model.entity.Module;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhz.senbeiforummain.model.vo.ModuleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 吉良吉影
* @description 针对表【module(模块表)】的数据库操作Mapper
* @createDate 2022-11-13 16:08:53
* @Entity com.yhz.senbeiforummain.domain.Module
*/
public interface ModuleMapper extends BaseMapper<Module> {
    ModuleVo getModuleVoById(@Param("id") Long id);

    /**
     * 根据id进行批量更新
     * @param moduleList
     * @return
     */
    int updateBatchById(@Param("moduleList") List<Module> moduleList);
}




