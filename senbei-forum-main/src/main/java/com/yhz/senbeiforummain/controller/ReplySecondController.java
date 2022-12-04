package com.yhz.senbeiforummain.controller;

import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.domain.dto.ReplySecondDto;
import com.yhz.senbeiforummain.service.IReplySecondService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("二级回复的回复接口")
    public BaseResponse reply(@RequestBody @Valid ReplySecondDto replySecondDto) {
        replySecondService.reply(replySecondDto);
        return ResultUtils.success();
    }
}
