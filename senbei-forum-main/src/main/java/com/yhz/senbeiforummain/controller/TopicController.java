package com.yhz.senbeiforummain.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.domain.dto.ModuleTopicPageDto;
import com.yhz.senbeiforummain.domain.dto.PublishTopicDto;
import com.yhz.senbeiforummain.domain.vo.TopicDetailVo;
import com.yhz.senbeiforummain.domain.vo.ModuleTopicVo;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.service.IModuleTopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author yanhuanzhan
 * @date 2022/11/13 - 21:49
 */
@RestController
@Api(tags = "主贴")
@RequestMapping("/module-topic")
public class TopicController {
    @Resource
    private IModuleTopicService moduleTopicService;
    @ApiOperation("分页查询接口")
    @PostMapping("/page")
    public BaseResponse<IPage<ModuleTopicVo>> pageModuleTopic(@RequestBody ModuleTopicPageDto moduleTopicPageDto) throws BusinessException {
        IPage<ModuleTopicVo> moduleTopicVoIPage = moduleTopicService.pageList(moduleTopicPageDto);
        return ResultUtils.success(moduleTopicVoIPage);
    }

    @PostMapping("/publish")
    @ApiOperation("主贴发布接口")
    @PreAuthorize("hasRole('user')")
    public BaseResponse publish( @RequestBody @Valid PublishTopicDto publishTopicDto) {
        moduleTopicService.publish(publishTopicDto);
        return ResultUtils.success();
    }

    @GetMapping("/detail/{topicId}")
    @ApiOperation("主贴详细信息")
    public BaseResponse<TopicDetailVo> detail(@PathVariable Long topicId) {
        TopicDetailVo topicDetailVo = moduleTopicService.getTopicDetailVo(topicId);
        return ResultUtils.success(topicDetailVo);
    }
}
