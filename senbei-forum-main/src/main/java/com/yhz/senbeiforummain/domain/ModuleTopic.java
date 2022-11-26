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
 * @TableName module_topic
 */
@TableName(value ="module_topic")
@Data
public class ModuleTopic implements Serializable {
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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 逻辑删除（0-删除，1-不删除)
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号
     */
    @Version
    private Integer version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}