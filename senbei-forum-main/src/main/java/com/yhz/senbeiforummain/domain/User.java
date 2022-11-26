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
 * 
 * @TableName t_user
 */
@TableName(value ="t_user")
@Data
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 积分
     */
    private Long point;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 个人描述
     */
    private String description;
    /**
     * 性别(0-未知,1-男,2-女人)
     */
    private Integer sex;

    /**
     * 用户头像
     */
    private String headUrl;
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
     * 逻辑删除(0-不删除 , 1-删除)
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