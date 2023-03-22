package com.yhz.senbeiforummain.model.vo;

import lombok.Data;

import java.util.Date;

/**
 * 聊天记录展示项
 * @author yanhuanzhan
 * @date 2023/3/20 - 17:39
 */
@Data
public class RoomChatRecordVo {
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
}
