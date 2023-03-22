package com.yhz.senbeiforummain.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 聊天室聊天记录表
 * @author 吉良吉影
 * @TableName room_chat_record
 */
@TableName(value ="room_chat_record")
@Data
public class RoomChatRecord implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 聊天室房间号
     */
    private String roomId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String headUrl;

    /**
     * 聊天内容
     */
    private String message;

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