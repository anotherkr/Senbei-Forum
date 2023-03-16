package com.yhz.senbeiforummain.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class TopicVo {

    private Long id;
    /**
     * 模块id
     */
    @ApiModelProperty("模块id")
    private Long moduleId;
    @ApiModelProperty("模块名")
    private String moduleName;
    /**
     * 主贴标题
     */
    @ApiModelProperty("主贴标题")
    private String title;

    @ApiModelProperty("主贴内容")
    private String content;

    @ApiModelProperty("图片地址(0-3张)")
    private String[] imgUrlArray;

    /**
     * 城市
     */
    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("用户基本信息")
    private UserInfoVo userInfoVo;


    @ApiModelProperty("回复数")
    private Integer replyNum;

    @ApiModelProperty("是否已点赞(0-未点赞,1-已点赞)")
    private int isSupport;
    @ApiModelProperty("点赞数")
    private Integer supportNum;

    @ApiModelProperty("点踩数")
    private Integer unsupportNum;

    @ApiModelProperty("热度")
    private Integer heat;
    @ApiModelProperty("创建时间")
    private Date createTime;
}
