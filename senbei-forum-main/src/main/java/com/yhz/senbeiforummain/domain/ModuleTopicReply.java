package com.yhz.senbeiforummain.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

/**
 * 主贴回复表
 * @author 吉良吉影
 * @TableName module_topic_reply
 */
@TableName(value ="module_topic_reply")
@Data
public class ModuleTopicReply extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 主贴id
     */
    private Long topicId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 回复内容
     */
    private String content;
    /**
     * 多图片地址
     */
    private String imgUrls;
    /**
     * 点赞数
     */
    private Integer supportNum;

    /**
     * 点踩数
     */
    private Integer unsupportNum;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}