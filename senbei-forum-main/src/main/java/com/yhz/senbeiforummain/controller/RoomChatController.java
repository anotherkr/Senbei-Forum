package com.yhz.senbeiforummain.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.constant.rediskey.RedisRoomChatRecordKey;
import com.yhz.senbeiforummain.model.dto.roomchat.RoomChatRecordQueryRequest;
import com.yhz.senbeiforummain.model.entity.RoomChatRecord;
import com.yhz.senbeiforummain.model.vo.RoomChatRecordVo;
import com.yhz.senbeiforummain.service.RoomChatRecordService;
import com.yhz.senbeiforummain.util.GsonUtil;
import com.yhz.senbeiforummain.util.RedisCache;
import com.yhz.senbeiforummain.ws.WebSocketServerEndpoint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author yanhuanzhan
 * @date 2023/3/22 - 11:10
 */
@RequestMapping("/room-chat")
@RestController
@Slf4j
@Api(tags = "聊天室模块")
public class RoomChatController {
    @Resource
    private RoomChatRecordService roomChatRecordService;


    @PostMapping("/records")
    @ApiOperation("获取聊天记录")
    public BaseResponse<List<RoomChatRecordVo>> getRoomChatRecords(@RequestBody RoomChatRecordQueryRequest roomChatRecordQueryRequest) {
        String roomId = roomChatRecordQueryRequest.getRoomId();
        Integer count = roomChatRecordQueryRequest.getCount();
        String id = roomChatRecordQueryRequest.getId();
        List<RoomChatRecordVo> res = roomChatRecordService.getRoomChatRecords(roomId,id,count);
        return ResultUtils.success(res);
    }

}
