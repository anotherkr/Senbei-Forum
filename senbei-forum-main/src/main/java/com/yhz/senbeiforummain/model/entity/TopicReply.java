package com.yhz.senbeiforummain.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.yhz.senbeiforummain.common.BaseEntity;
import lombok.Data;

/**
 * 主贴回复表
 * @author 吉良吉影
 * @TableName topic_reply
 */
@TableName(value ="topic_reply")
@Data
public class TopicReply extends BaseEntity implements Serializable {
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

    private String city;
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