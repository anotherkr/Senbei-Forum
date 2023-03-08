package com.yhz.senbeiforummain.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author yanhuanzhan
 * @date 2022/11/27 - 15:12
 */
@Data
@ApiModel("主贴回复展示项")
public class TopicReplyVo {

    @ApiModelProperty("主贴回复id")
    private Long id;

    @ApiModelProperty("用户基本信息")
    private UserInfoVo userInfoVo;

    @ApiModelProperty("回复内容")
    private String content;

    private String city;

    @ApiModelProperty("图片地址")
    @TableField(exist = false)
    private String[] imgUrlArray;

    @ApiModelProperty("二级回复")
    private List<ReplySecondVo> replySecondVoList;

    @ApiModelProperty("点赞数")
    private Integer supportNum;

    @ApiModelProperty("点踩数")
    private Integer unsupportNum;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
