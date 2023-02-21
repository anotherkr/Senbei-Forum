package com.yhz.senbeiforummain.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.model.dto.topic.TopicQueryRequest;
import com.yhz.senbeiforummain.model.dto.topic.TopicAddRequst;
import com.yhz.senbeiforummain.model.vo.TopicDetailVo;
import com.yhz.senbeiforummain.model.vo.TopicVo;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.service.ITopicService;
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

/**
 * @author yanhuanzhan
 * @date 2022/11/13 - 21:49
 */
@RestController
@Api(tags = "主贴")
@RequestMapping("/module-topic")
public class TopicController {
    @Resource
    private ITopicService topicService;
    @ApiOperation("分页查询接口")
    @PostMapping("/page")
    public BaseResponse<IPage<TopicVo>> pageModuleTopic(@RequestBody TopicQueryRequest topicQueryRequest) throws BusinessException {
        IPage<TopicVo> topicVoIPage = topicService.pageList(topicQueryRequest);
        return ResultUtils.success(topicVoIPage);
    }

    @PostMapping("/publish")
    @ApiOperation("主贴发布接口")
    //@PreAuthorize("hasRole('user')")
    public BaseResponse publish( @RequestBody @Valid TopicAddRequst topicAddRequst) {
        topicService.publish(topicAddRequst);
        return ResultUtils.success();
    }

    @GetMapping("/detail/{topicId}")
    @ApiOperation("主贴详细信息")
    public BaseResponse<TopicDetailVo> detail(@PathVariable Long topicId) {
        TopicDetailVo topicDetailVo = topicService.getTopicDetailVo(topicId);
        return ResultUtils.success(topicDetailVo);
    }
}
