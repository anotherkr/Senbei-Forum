package com.yhz.senbeiforummain.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.yhz.senbeiforummain.common.BaseEntity;
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
public class Module extends BaseEntity implements Serializable {
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
     * 背景图片
     */
    private String backgroundImgUrl;

    /**
     * 模块描述
     */
    private String statement;
    /**
     * 关注数
     */
    private Long concernNum;
    /**
     * 热度
     */
    private Integer heat;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}