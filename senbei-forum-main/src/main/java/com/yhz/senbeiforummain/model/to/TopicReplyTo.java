package com.yhz.senbeiforummain.model.to;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yhz.senbeiforummain.model.vo.ReplySecondVo;
import com.yhz.senbeiforummain.model.vo.UserInfoVo;
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
public class TopicReplyTo {

    private Long id;

    private UserInfoVo userInfoVo;

    private String content;

    private String imgUrls;

    private List<ReplySecondVo> replySecondVoList;

    private Integer supportNum;

    private Integer unsupportNum;

    private Date createTime;

}
