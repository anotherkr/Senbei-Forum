package com.yhz.senbeiforummain.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.commonutil.common.BaseResponse;
import com.yhz.senbeiforummain.domain.dto.ModulePageDto;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.domain.Module;
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
     * @param modulePageDto
     * @return
     */
    @ApiOperation("分页查询接口")
    @PostMapping("/page")
    public BaseResponse<IPage<Module>> pageList(@RequestBody ModulePageDto modulePageDto) {
        IPage<Module> page = moduleService.pageList(modulePageDto);
        return ResultUtils.success(page);
    }

}
