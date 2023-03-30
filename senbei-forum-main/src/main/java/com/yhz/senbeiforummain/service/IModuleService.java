package com.yhz.senbeiforummain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.senbeiforummain.model.dto.module.ModuleQueryRequest;
import com.yhz.senbeiforummain.model.entity.Module;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhz.senbeiforummain.model.vo.ModuleVo;

import java.util.List;

/**
* @author 吉良吉影
* @description 针对表【module(模块表)】的数据库操作Service
* @createDate 2022-11-13 16:08:53
*/
public interface IModuleService extends IService<Module> {

    IPage<ModuleVo> pageList(ModuleQueryRequest moduleQueryRequest);

    ModuleVo getModuleVo(Long moduleId, Long userId);
    /**
     * 校准所有模块的关注数
     * @return
     */
    int calibrationConcern();
    /**
     * 校准所有模块的帖子数
     * @return
     */
    int calibrationTopicNum();

    /**
     * 获取推荐模块
     * @param count
     * @return
     */
    List<ModuleVo> getRecommendTopic(Integer count);
}
