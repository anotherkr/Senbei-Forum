package com.yhz.senbeiforummain.model.dto.websocket;

import lombok.Data;

import java.util.Date;

/**
 * 前端发送给服务器的信息
 * @author yanhuanzhan
 * @date 2023/3/20 - 3:57
 */
@Data
public class MessageDTO {
    private String roomId;
    private String username;
    private String message;
    private Date createTime;
}
