package com.yhz.senbeiforummain.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.yhz.senbeiforummain.common.BaseEntity;
import lombok.Data;

/**
 * 模块关注表
 * @TableName module_concern
 */
@TableName(value ="module_concern")
@Data
public class ModuleConcern extends BaseEntity implements Serializable {
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
     * 是否关注(0-未关注 1-关注)
     */
    private Integer isConcern;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}