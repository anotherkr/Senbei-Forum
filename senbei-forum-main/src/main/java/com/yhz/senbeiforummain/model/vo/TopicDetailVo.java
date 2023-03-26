package com.yhz.senbeiforummain.model.vo;

/**
 * 展示主贴所有内容
 *
 * @author yanhuanzhan
 * @date 2022/11/27 - 15:04
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel("展示主贴所有内容")
@Data
public class TopicDetailVo {
    @ApiModelProperty("主贴id")
    private Long topicId;

    @ApiModelProperty("模块id")
    private Long moduleId;

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

    @ApiModelProperty("主贴回复集合")
    private IPage<TopicReplyVo> topicReplyVoIPage;

    @ApiModelProperty("访问量")
    private Long clickNum;

    @ApiModelProperty("回复数")
    private Integer replyNum;

    @ApiModelProperty("点赞数")
    private Integer supportNum;

    @ApiModelProperty("点踩数")
    private Integer unsupportNum;

    @ApiModelProperty("热度")
    private Integer heat;

    @ApiModelProperty("是否已点赞(0-未点赞,1-已点赞)")
    private int isSupport;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
