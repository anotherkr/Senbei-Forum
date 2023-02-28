package com.yhz.senbeiforummain.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.yhz.senbeiforummain.common.BaseEntity;
import lombok.Data;

/**
 * 主贴表
 *
 * @TableName topic
 */
@TableName(value = "topic")
@Data
public class Topic extends BaseEntity implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模块id
     */
    private Long moduleId;
    /**
     * 主贴内容(5-2000字)
     */
    private String content;
    /**
     * 图片地址(0-3张图片)
     */
    private String imgUrls;
    /**
     * 城市
     */
    private String city;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 回复数
     */
    private Integer replyNum;

    /**
     * 主贴标题
     */
    private String title;
    /**
     * 访问量
     */
    private Long clickNum;
    /**
     * 点赞数
     */
    private Integer supportNum;

    /**
     * 点踩数
     */
    private Integer unsupportNum;

    /**
     * 热度
     */
    private Integer heat;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}