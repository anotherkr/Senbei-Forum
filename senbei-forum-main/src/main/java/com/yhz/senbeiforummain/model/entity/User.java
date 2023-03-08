package com.yhz.senbeiforummain.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.yhz.senbeiforummain.common.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @author 吉良吉影
 * @TableName user
 */
@TableName(value ="user")
@Data
@Accessors(chain = true)
public class User extends BaseEntity implements Serializable {
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
     * 邮箱
     */
    private String email;
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
     * 用户等级(1-6)
     */
    private Integer level;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}