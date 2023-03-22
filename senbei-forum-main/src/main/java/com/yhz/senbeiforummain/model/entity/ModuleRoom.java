package com.yhz.senbeiforummain.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 模块聊天室关联表
 * @author 吉良吉影
 * @TableName module_room
 */
@TableName(value ="module_room")
@Data
public class ModuleRoom implements Serializable {
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
     * 聊天室房间号
     */
    private String roomId;
    /**
     * 聊天室名称
     */
    private String roomName;
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
    private Integer isDeleted;

    /**
     * 版本号
     */
    private Integer version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}