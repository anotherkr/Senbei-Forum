package com.yhz.senbeiforummain.constant.rediskey;

import com.yhz.commonutil.common.RedisBasePrefixKey;

/**
 * 聊天记录redis前缀
 * @author yanhuanzhan
 * @date 2023/3/22 - 11:36
 */
public class RedisRoomChatRecordKey extends RedisBasePrefixKey {
    public static final String ROOM_CHAT_RECORD = "roomChatRecord";

    /**
     * 仅前缀 hash(roomId->Json(Deque<RoomChatRecord>))
     */
    public static RedisRoomChatRecordKey getRoomChatRecord = new RedisRoomChatRecordKey(ROOM_CHAT_RECORD,-1L);

    private RedisRoomChatRecordKey(String prefix) {
        super(prefix);
    }

    private RedisRoomChatRecordKey(String prefix,Long expireSeconds) {
        super(prefix,expireSeconds);
    }
}
