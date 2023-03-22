package com.yhz.senbeiforummain.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.senbeiforummain.constant.rediskey.RedisRoomChatRecordKey;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.model.entity.RoomChatRecord;
import com.yhz.senbeiforummain.model.vo.RoomChatRecordVo;
import com.yhz.senbeiforummain.service.RoomChatRecordService;
import com.yhz.senbeiforummain.mapper.RoomChatRecordMapper;
import com.yhz.senbeiforummain.util.GsonUtil;
import com.yhz.senbeiforummain.util.RedisCache;
import com.yhz.senbeiforummain.ws.WebSocketServerEndpoint;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author 吉良吉影
 * @description 针对表【room_chat_record(聊天室聊天记录表)】的数据库操作Service实现
 * @createDate 2023-03-20 17:10:44
 */
@Service
public class RoomChatRecordServiceImpl extends ServiceImpl<RoomChatRecordMapper, RoomChatRecord>
        implements RoomChatRecordService {
    @Resource
    private RedisCache redisCache;

    @Override
    public List<RoomChatRecordVo> getRoomChatRecords(String roomId, String id, Integer count) {
        if (StrUtil.isBlank(roomId)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //先从内存拿聊天记录
        Deque<RoomChatRecordVo> roomChatRecordVoDeque = WebSocketServerEndpoint.roomChatRecordMap.get(roomId);
        List<RoomChatRecordVo> roomChatRecordVos;
        if (roomChatRecordVoDeque == null) {
            roomChatRecordVos = new ArrayList<>();
        } else {
            roomChatRecordVos = new ArrayList<>(roomChatRecordVoDeque);
        }
        if (roomChatRecordVos == null || roomChatRecordVos.isEmpty()) {
            //如果为空,先从redis中拿
            Map<String, String> cacheMap = redisCache.getCacheMap(RedisRoomChatRecordKey.getRoomChatRecord, "");
            if (cacheMap != null && !cacheMap.isEmpty()) {
                String json = cacheMap.get(roomId);
                roomChatRecordVos = GsonUtil.strToList(json, RoomChatRecordVo.class);
            }

        }
        if (roomChatRecordVos == null || roomChatRecordVos.isEmpty()) {
            //如果还是为空则从数据库拿
            QueryWrapper<RoomChatRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("room_id", roomId);
            queryWrapper.orderByAsc("create_time");

            roomChatRecordVos = this.listObjs(queryWrapper, item -> {
                RoomChatRecordVo roomChatRecordVo = new RoomChatRecordVo();
                BeanUtils.copyProperties(item, roomChatRecordVo);
                return roomChatRecordVo;
            });
        }
        Collections.sort(roomChatRecordVos,(a,b)->{
            if (a.getCreateTime().getTime() > b.getCreateTime().getTime()) {
                return -1;
            }else {
                return 1;
            }
        });
        List<RoomChatRecordVo> res = new ArrayList<>();
        int index = roomChatRecordVos.size() - 1;
        if (id != null) {
            for (int i = index; i >= 0; i--) {
                RoomChatRecordVo roomChatRecordVo = roomChatRecordVos.get(i);
                if (roomChatRecordVo != null && id.equals(roomChatRecordVo.getId())) {
                    index = i - 1;
                }
            }
        }
        if (roomChatRecordVos != null) {
            WebSocketServerEndpoint.roomChatRecordMap.put(roomId, new ArrayDeque<>(roomChatRecordVos));
            while (index >= 0 && count > 0) {
                res.add(roomChatRecordVos.get(index));
                count--;
                index--;
            }
        }
        Collections.reverse(res);
        return res;
    }
}




