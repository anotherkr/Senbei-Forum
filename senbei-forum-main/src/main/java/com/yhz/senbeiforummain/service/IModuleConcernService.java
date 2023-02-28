package com.yhz.senbeiforummain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.senbeiforummain.model.dto.moduleconcern.ModuleConcernAddRequest;
import com.yhz.senbeiforummain.model.dto.moduleconcern.ModuleConcernQueryRequest;
import com.yhz.senbeiforummain.model.dto.moduleconcern.ModuleConcernRemoveRequest;
import com.yhz.senbeiforummain.model.entity.ModuleConcern;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhz.senbeiforummain.model.vo.ModuleConcernTopicVo;

/**
* @author 吉良吉影
* @description 针对表【module_concern(模块关注表)】的数据库操作Service
* @createDate 2023-02-20 13:09:54
*/
public interface IModuleConcernService extends IService<ModuleConcern> {
    /**
     * 关注模块
     * @param moduleConcernAddRequest
     * @param userId
     * @return
     */
    boolean addModuleConcern(ModuleConcernAddRequest moduleConcernAddRequest, Long userId);

    /**
     * 获取用户所关注模块的主贴并分页
     * @param moduleConcernQueryRequest
     * @return
     */
    IPage<ModuleConcernTopicVo> getModuleConcernTopicByPage(ModuleConcernQueryRequest moduleConcernQueryRequest);



    /**
     * 取消关注模块
     * @param moduleConcernRemoveRequest
     */
    void cancelModuleConcern(ModuleConcernRemoveRequest moduleConcernRemoveRequest);
}
