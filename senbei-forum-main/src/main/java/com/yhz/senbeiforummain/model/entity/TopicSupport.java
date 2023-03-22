package com.yhz.senbeiforummain.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.yhz.senbeiforummain.common.BaseEntity;
import lombok.Data;

/**
 * 帖子点赞表
 * @TableName topic_support
 */
@TableName(value ="topic_support")
@Data
public class TopicSupport extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 帖子id
     */
    private Long topicId;

    /**
     * 是否点赞(0-未点赞，1-已点赞)
     */
    private Integer isSupport;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}