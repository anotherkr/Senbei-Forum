package com.yhz.senbeiforummain.controller;

import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.common.annotation.UserId;
import com.yhz.senbeiforummain.model.dto.topicreply.TopicReplyAddRequst;
import com.yhz.senbeiforummain.model.enums.SupportEnum;
import com.yhz.senbeiforummain.service.ITopicReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public BaseResponse reply(@RequestBody @Valid TopicReplyAddRequst topicReplyAddRequst, @UserId Long userId, HttpServletRequest request) {
        moduleTopicReplyService.reply(topicReplyAddRequst, userId, request);
        return ResultUtils.success();
    }

    @ApiOperation(value = "点赞功能")
    @PostMapping("/support/{topicReplyId}")
    public BaseResponse support(@PathVariable("topicReplyId") Long topicReplyId,@UserId Long userId) {
        if (userId == null) {
            //-1代表匿名用户
            userId=-1L;
        }
        //返回缓存中点赞状态，执行相反操作
        Integer support = moduleTopicReplyService.support(topicReplyId, userId);
        System.out.println(support);
        if (support==null||support.equals(SupportEnum.NO_SUPPORT.getCode())) {
            return ResultUtils.success("点赞成功");
        }else {
            return ResultUtils.success("点赞取消");
        }

    }
}
