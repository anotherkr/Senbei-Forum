package com.yhz.senbeiforummain.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.commonutil.BaseResponse;
import com.yhz.commonutil.ResultUtils;
import com.yhz.senbeiforummain.domain.ModuleTopic;
import com.yhz.senbeiforummain.domain.dto.ModuleTopicPageDto;
import com.yhz.senbeiforummain.domain.vo.ModuleTopicVo;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.service.IModuleTopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yanhuanzhan
 * @date 2022/11/13 - 21:49
 */
@RestController
@Api(tags = "主贴")
@RequestMapping("/module-topic")
public class ModuleTopicController {
    @Resource
    private IModuleTopicService moduleTopicService;
    @ApiOperation("分页查询接口")
    @PostMapping("/page")
    public BaseResponse<IPage<ModuleTopicVo>> pageModuleTopic(@RequestBody ModuleTopicPageDto moduleTopicPageDto) throws BusinessException {
        IPage<ModuleTopicVo> moduleTopicVoIPage = moduleTopicService.pageList(moduleTopicPageDto);
        return ResultUtils.success(moduleTopicVoIPage);
    }
}
