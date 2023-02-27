package com.yhz.senbeiforummain.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.yhz.senbeiforummain.common.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 吉良吉影
 * @TableName third_user
 */
@TableName(value = "third_user")
@Data
@Accessors(chain = true)
public class ThirdUser extends BaseEntity implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 第三方id
     */
    private Long thirdId;
    /**
     * 渠道(0-github,1-gitee)
     */
    private Integer channel;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 账号
     */
    private String username;

    /**
     * 用户头像
     */
    private String headUrl;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}