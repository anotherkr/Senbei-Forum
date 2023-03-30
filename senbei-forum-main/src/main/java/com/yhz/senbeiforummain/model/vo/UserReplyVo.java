package com.yhz.senbeiforummain.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户回复展示项
 * @author yanhuanzhan
 * @date 2023/3/26 - 17:37
 */
@Data
public class UserReplyVo {
    @ApiModelProperty("模块id")
    private Long moduleId;

    @ApiModelProperty("主贴")
    private Long topicId;
    /**
     * 模块名
     */
    @ApiModelProperty("模块名")
    private String moduleName;
    /**
     * 主贴标题
     */
    @ApiModelProperty("主贴标题")
    private String title;

    @ApiModelProperty("回复内容")
    private String content;

    @ApiModelProperty("点赞数")
    private Integer supportNum;
    @ApiModelProperty("图片地址")
    @TableField(exist = false)
    private String[] imgUrlArray;

    /**
     * 城市
     */
    @ApiModelProperty("城市")
    private String city;
    @ApiModelProperty("用户基本信息")
    private UserInfoVo userInfoVo;
    @ApiModelProperty("热度")
    private Integer heat;
    @ApiModelProperty("创建时间")
    private Date createTime;
}
