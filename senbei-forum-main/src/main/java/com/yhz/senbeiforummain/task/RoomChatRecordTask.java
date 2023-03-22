package com.yhz.senbeiforummain.task;

import com.yhz.senbeiforummain.constant.rediskey.RedisRoomChatRecordKey;
import com.yhz.senbeiforummain.constant.rediskey.RedisTopicReplyKey;
import com.yhz.senbeiforummain.model.entity.RoomChatRecord;
import com.yhz.senbeiforummain.model.entity.TopicReplySupport;
import com.yhz.senbeiforummain.model.vo.RoomChatRecordVo;
import com.yhz.senbeiforummain.service.RoomChatRecordService;
import com.yhz.senbeiforummain.util.GsonUtil;
import com.yhz.senbeiforummain.util.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * @author yanhuanzhan
 * @date 2023/3/22 - 11:44
 */
@Component

public class RoomChatRecordTask {
    @Resource
    private RedisCache redisCache;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RoomChatRecordService roomChatRecordService;
    /**
     * 秒 分 时 日 月 周
     * 以秒为例
     * *：每秒都执行
     * 1-3：从第1秒开始执行，到第3秒结束执行
     * 0/3：从第0秒开始，每隔3秒执行1次
     * 1,2,3：在指定的第1、2、3秒执行
     * ?：不指定
     * 日和周不能同时制定，指定其中之一，则另一个设置为?
     */


    /**
     * 记录聊天记录(每天0点触发)
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void roomChatRecordTask() {
        Cursor<Map.Entry<String, String>> cursor = redisTemplate.opsForHash().scan(RedisRoomChatRecordKey.getRoomChatRecord.getPrefix(), ScanOptions.NONE);
        while (cursor.hasNext()) {
            Map.Entry<String,String> next = cursor.next();

            String json = next.getValue();
            ArrayDeque<RoomChatRecordVo> roomChatRecordVoDeque = GsonUtil.strToJavaBean(json, ArrayDeque.class);
            List<RoomChatRecord> roomChatRecordList = new ArrayList<>();
            roomChatRecordVoDeque.forEach(item->{
                RoomChatRecord roomChatRecord = new RoomChatRecord();
                BeanUtils.copyProperties(item, roomChatRecord);
                roomChatRecordList.add(roomChatRecord);
            });
            roomChatRecordService.saveOrUpdateBatch(roomChatRecordList);
        }
    }
}
