package com.yhz.senbeiforummain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.senbeiforummain.model.dto.module.ModuleQueryRequest;
import com.yhz.senbeiforummain.model.entity.Module;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhz.senbeiforummain.model.vo.ModuleVo;

/**
* @author 吉良吉影
* @description 针对表【module(模块表)】的数据库操作Service
* @createDate 2022-11-13 16:08:53
*/
public interface IModuleService extends IService<Module> {

    IPage<ModuleVo> pageList(ModuleQueryRequest moduleQueryRequest);
}
