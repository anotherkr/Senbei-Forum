package com.yhz.senbeiforummain.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

/**
 * 二级回复表
 * @author 吉良吉影
 * @TableName reply_second
 */
@TableName(value ="reply_second")
@Data
public class ReplySecond extends BaseEntity implements Serializable {

    @TableId
    private Long id;

    /**
     * 一级回复id
     */
    private Long topicReplyId;

    /**
     * 二级回复的回复内容
     */
    private String content;

    /**
     * 用户id
     */
    private Long userId;

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