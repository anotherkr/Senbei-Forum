package com.yhz.senbeiforummain.model.dto.replysecond;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author yanhuanzhan
 * @date 2022/11/27 - 12:51
 */
@Data
public class ReplySecondQueryRequst {
    @ApiModelProperty("一级回复id")
    private Long topicReplyId;

    @ApiModelProperty("二级回复的回复内容(1-200字)")
    @Size(min = 1,max = 200,message = "回复内容应为1-200字之间")
    private String content;

    @ApiModelProperty("用户id")
    private Long userId;
}
