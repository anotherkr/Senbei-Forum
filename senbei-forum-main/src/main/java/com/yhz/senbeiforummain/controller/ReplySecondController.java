package com.yhz.senbeiforummain.controller;

import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.common.annotation.UserId;
import com.yhz.senbeiforummain.model.dto.replysecond.ReplySecondQueryRequst;
import com.yhz.senbeiforummain.service.IReplySecondService;
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
 * @date 2022/11/27 - 12:48
 */
@RestController
@Api(tags = "二级回复")
@RequestMapping("/reply-second")
public class ReplySecondController {
    @Resource
    private IReplySecondService replySecondService;
    @PostMapping("/reply")
    @PreAuthorize("hasRole('user')")
    @ApiOperation("二级回复的回复接口")
    public BaseResponse reply(@RequestBody @Valid ReplySecondQueryRequst replySecondQueryRequst, @UserId Long userId) {
        replySecondService.reply(replySecondQueryRequst,userId);
        return ResultUtils.success();
    }
}
