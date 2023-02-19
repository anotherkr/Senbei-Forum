package com.yhz.senbeiforummain.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.commonutil.common.BaseResponse;
import com.yhz.senbeiforummain.model.dto.module.ModuleQueryRequest;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.model.entity.Module;
import com.yhz.senbeiforummain.model.vo.ModuleVo;
import com.yhz.senbeiforummain.service.IModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
     * @param moduleQueryRequest
     * @return
     */
    @ApiOperation("分页查询接口")
    @PostMapping("/page")
    public BaseResponse<IPage<ModuleVo>> pageList(@RequestBody ModuleQueryRequest moduleQueryRequest) {
        IPage<ModuleVo> page = moduleService.pageList(moduleQueryRequest);
        return ResultUtils.success(page);
    }

}
