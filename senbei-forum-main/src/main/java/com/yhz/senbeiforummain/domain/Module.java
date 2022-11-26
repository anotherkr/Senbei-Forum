package com.yhz.senbeiforummain.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 模块表
 * @TableName module
 */
@TableName(value ="module")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Module implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 模块名
     */
    private String name;

    /**
     * 模块图片地址
     */
    private String imgUrl;

    /**
     * 主贴数目
     */
    private Long topicNum;

    /**
     * 点击数
     */
    private Long clickNum;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 模块描述
     */
    private String statement;

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
     * 逻辑删除（0-删除，1-不删除)
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 版本号
     */
    @Version
    private Integer version;

    /**
     * 更新者
     */
    private String updateBy;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}