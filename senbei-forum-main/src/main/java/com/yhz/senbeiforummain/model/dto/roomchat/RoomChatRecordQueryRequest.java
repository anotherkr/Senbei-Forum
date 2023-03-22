package com.yhz.senbeiforummain.model.dto.roomchat;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

/**
 * @author yanhuanzhan
 * @date 2023/3/22 - 20:06
 */
@Data
public class RoomChatRecordQueryRequest {
    private String id;
    private Integer count;
    @NotNull(message = "房间号不能为空")
    private String roomId;
}
