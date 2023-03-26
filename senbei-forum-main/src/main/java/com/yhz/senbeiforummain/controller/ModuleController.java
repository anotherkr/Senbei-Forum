package com.yhz.senbeiforummain.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.commonutil.common.BaseResponse;
import com.yhz.senbeiforummain.common.annotation.UserId;
import com.yhz.senbeiforummain.model.dto.module.ModuleQueryRequest;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.model.vo.ModuleVo;
import com.yhz.senbeiforummain.model.vo.TopicVo;
import com.yhz.senbeiforummain.service.IModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yanhuanzhan
 * @date 2022/11/13 - 18:03
 */
@RestController
@Slf4j
@Api(tags = "模块")
@RequestMapping("/module")
public class ModuleController {
    @Resource
    public IModuleService moduleService;

    /**
     * 分页查询接口
     *
     * @param moduleQueryRequest
     * @return
     */
    @ApiOperation("分页查询接口")
    @PostMapping("/page")
    public BaseResponse<IPage<ModuleVo>> pageList(@RequestBody ModuleQueryRequest moduleQueryRequest) {
        IPage<ModuleVo> page = moduleService.pageList(moduleQueryRequest);
        return ResultUtils.success(page);
    }

    @ApiOperation("获取模块信息")
    @GetMapping("/one")
    public BaseResponse<ModuleVo> getOne(Long moduleId, @UserId Long userId) {
        ModuleVo moduleVo = moduleService.getModuleVo(moduleId, userId);
        return ResultUtils.success(moduleVo);
    }
    @ApiOperation(value = "获取推荐模块")
    @GetMapping("/recommend/{count}")
    public BaseResponse getRecommendTopic(@PathVariable("count")Integer count) {
        List<ModuleVo> moduleVos=moduleService.getRecommendTopic(count);
        return ResultUtils.success(moduleVos);
    }
}
