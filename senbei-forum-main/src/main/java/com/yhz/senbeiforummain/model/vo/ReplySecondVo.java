package com.yhz.senbeiforummain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yanhuanzhan
 * @date 2022/11/27 - 15:19
 */
@Data
@ApiModel("二级回复展示项")
public class ReplySecondVo {
    @ApiModelProperty("二级回复id")
    private Long id;

    @ApiModelProperty("二级回复的回复内容")
    private String content;

    @ApiModelProperty("用户基本信息")
    private UserInfoVo userInfoVo;

    @ApiModelProperty("点赞数")
    private Integer supportNum;

    @ApiModelProperty("点踩数")
    private Integer unsupportNum;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
