package com.yhz.senbeiforummain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Deque;
import java.util.List;

/**
 * @author 吉良吉影
 */
@Data
@ApiModel(description = "websocket消息内容")
public class MessagegVO {

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "用户昵称")
    private String nickname;
    @ApiModelProperty(value = "用户头像")
    private String headUrl;
    ;

    @ApiModelProperty(value = "消息")
    private Object message;

    @ApiModelProperty(value = "在线人数")
    private int count;
    /**
     * 是否为系统消息
     */
    private Boolean isSystem;
    /**
     * 用户列表
     */
    private List<UserInfoVo> userInfoVoList;
}