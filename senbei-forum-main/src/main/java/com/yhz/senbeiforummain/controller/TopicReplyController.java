package com.yhz.senbeiforummain.controller;

import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.model.dto.topicreply.TopicReplyRequst;
import com.yhz.senbeiforummain.service.ITopicReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author yanhuanzhan
 * @date 2022/11/27 - 2:52
 */
@RestController
@Api(tags = "主贴回复")
@RequestMapping("/module-topic-reply")
public class TopicReplyController {
    @Resource
    private ITopicReplyService moduleTopicReplyService;

    @PostMapping("/reply")
    @PreAuthorize("hasRole('user')")
    @ApiOperation("主贴回复接口")
    public BaseResponse reply(@RequestBody @Valid TopicReplyRequst topicReplyRequst) {
        moduleTopicReplyService.reply(topicReplyRequst);
        return ResultUtils.success();
    }
}
