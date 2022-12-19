package com.yhz.senbeiforummain.controller;

import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.domain.dto.ModuleTopicReplyDto;
import com.yhz.senbeiforummain.service.IModuleTopicReplyService;
import io.swagger.annotations.Api;
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
    private IModuleTopicReplyService moduleTopicReplyService;

    @PostMapping("/reply")
    @PreAuthorize("hasRole('user')")
    public BaseResponse reply(@RequestBody @Valid ModuleTopicReplyDto moduleTopicReplyDto) {
        moduleTopicReplyService.reply(moduleTopicReplyDto);
        return ResultUtils.success();
    }
}
