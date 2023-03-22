package com.yhz.senbeiforummain.service;

import com.yhz.senbeiforummain.model.entity.RoomChatRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhz.senbeiforummain.model.vo.RoomChatRecordVo;

import java.util.List;

/**
* @author 吉良吉影
* @description 针对表【room_chat_record(聊天室聊天记录表)】的数据库操作Service
* @createDate 2023-03-20 17:10:44
*/
public interface RoomChatRecordService extends IService<RoomChatRecord> {
    /**
     * 获取聊天记录
     * @param roomId 房间id
     * @param id RoomChatRecord表id
     * @param count 每次获取记录的条数
     * @return
     */
    List<RoomChatRecordVo> getRoomChatRecords(String roomId, String id, Integer count);
}
