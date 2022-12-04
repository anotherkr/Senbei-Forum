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
 * 主贴表
 *
 * @TableName module_topic
 */
@TableName(value = "module_topic")
@Data
public class ModuleTopic extends BaseEntity implements Serializable {
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