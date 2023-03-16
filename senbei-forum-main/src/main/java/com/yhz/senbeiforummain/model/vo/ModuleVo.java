package com.yhz.senbeiforummain.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanhuanzhan
 * @date 2023/2/19 - 19:53
 */
@Data
@ApiModel("模块信息展示项")
public class ModuleVo {

    private Long id;

    /**
     * 模块名
     */
    @ApiModelProperty("模块名")
    private String name;

    /**
     * 模块图片地址
     */
    @ApiModelProperty("模块图片地址")
    private String imgUrl;

    /**
     * 主贴数目
     */
    @ApiModelProperty("主贴数目")
    private Long topicNum;

    /**
     * 点击数
     */
    @ApiModelProperty("点击数")
    private Long clickNum;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 背景图片
     */
    @ApiModelProperty("背景图片")
    private String backgroundImgUrl;

    /**
     * 模块描述
     */
    @ApiModelProperty("模块描述")
    private String statement;

    /**
     * user数
     */
    @ApiModelProperty("关注数")
    private Long concernNum;
    /**
     * 热度
     */
    @ApiModelProperty("热度")
    private Integer heat;

    @ApiModelProperty("是否已关注(0-未关注,1-关注)")
    private Integer isConcern;

}
