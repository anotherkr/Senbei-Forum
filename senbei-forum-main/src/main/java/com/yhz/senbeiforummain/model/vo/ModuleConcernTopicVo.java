package com.yhz.senbeiforummain.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yanhuanzhan
 * @date 2022/11/25 - 19:13
 */
@Data
@ApiModel("关注模块的主贴展示项")
public class ModuleConcernTopicVo {

    private Long id;

    @ApiModelProperty("模块信息")
    private ModuleVo moduleVo;
    /**
     * 模块id
     */
    @ApiModelProperty("模块id")
    private Long moduleId;
    /**
     * 主贴标题
     */
    @ApiModelProperty("主贴标题")
    private String title;

    @ApiModelProperty("主贴内容")
    private String content;

    @ApiModelProperty("图片地址(0-3张)")
    private String[] imgUrlArray;

    @ApiModelProperty("用户基本信息")
    private UserInfoVo userInfoVo;


    @ApiModelProperty("回复数")
    private Integer replyNum;

    @ApiModelProperty("点赞数")
    private Integer supportNum;

    @ApiModelProperty
    private Integer isSupport;
    @ApiModelProperty("热度")
    private Integer heat;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
