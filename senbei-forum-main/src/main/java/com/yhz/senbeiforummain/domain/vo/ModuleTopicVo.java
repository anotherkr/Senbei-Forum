package com.yhz.senbeiforummain.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yanhuanzhan
 * @date 2022/11/25 - 19:13
 */
@Data
@ApiModel("主贴展示项")
public class ModuleTopicVo {

    private Long id;

    /**
     * 模块id
     */
    @ApiModelProperty("模块id")
    private Long moduleId;

    /**
     * 用户基本信息
     */
    @ApiModelProperty("用户基本信息")
    private UserInfoVo userInfoVo;

    /**
     * 回复数
     */
    @ApiModelProperty("回复数")
    private Integer replyNum;

    /**
     * 主贴标题
     */
    @ApiModelProperty("主贴标题")
    private String title;

    /**
     * 点赞数
     */
    @ApiModelProperty("点赞数")
    private Integer supportNum;

    /**
     * 点踩数
     */
    @ApiModelProperty("点踩数")
    private Integer unsupportNum;

    /**
     * 热度
     */
    @ApiModelProperty("热度")
    private Integer heat;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
}
