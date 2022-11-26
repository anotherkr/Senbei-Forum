package com.yhz.senbeiforummain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.senbeiforummain.domain.dto.ModulePageDto;
import com.yhz.senbeiforummain.domain.Module;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 吉良吉影
* @description 针对表【module(模块表)】的数据库操作Service
* @createDate 2022-11-13 16:08:53
*/
public interface IModuleService extends IService<Module> {

    IPage<Module> pageList(ModulePageDto modulePageDto);
}
