package com.yhz.senbeiforummain.ws;

import cn.hutool.core.util.IdUtil;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.senbeiforummain.constant.rediskey.RedisRoomChatRecordKey;
import com.yhz.senbeiforummain.constant.rediskey.RedisUserKey;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.model.dto.websocket.MessageDTO;
import com.yhz.senbeiforummain.model.entity.User;
import com.yhz.senbeiforummain.model.vo.RoomChatRecordVo;
import com.yhz.senbeiforummain.model.vo.UserInfoVo;
import com.yhz.senbeiforummain.security.domain.AuthUser;
import com.yhz.senbeiforummain.util.GsonUtil;
import com.yhz.senbeiforummain.util.JwtUtil;
import com.yhz.senbeiforummain.util.MessageUtils;
import com.yhz.senbeiforummain.util.RedisCache;
import com.yhz.senbeiforummain.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 吉良吉影
 */
@Slf4j
@Component
@ServerEndpoint("/groupChat/{roomId}/{authorization}")
public class WebSocketServerEndpoint {
    /**
     * 队列中存储的最大聊天记录数
     */
    public static final Integer CHAT_RECORD_MAX = 1000;
    /**
     * 房间号 -> map(用户名,Session)
     */
    private static ConcurrentHashMap<String, Map<String, Session>> roomSessionMap = new ConcurrentHashMap<>();
    /**
     * 房间号 -> map(用户名,用户信息)
     */
    private static ConcurrentHashMap<String, Map<String, UserInfoVo>> roomUserInfoMap = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<String, Deque<RoomChatRecordVo>> roomChatRecordMap = new ConcurrentHashMap<>();

    /**
     * 收到消息调用的方法，群成员发送消息
     *
     * @param roomId:房间号
     * @param message：发送消息
     */
    @OnMessage
    public void onMessage(@PathParam("roomId") String roomId, @PathParam("authorization") String authorization, String message) {
        //获取用户信息
        String currentUsername = JwtUtil.getUsername(authorization);
        Map<String, UserInfoVo> userInfoVoMap = roomUserInfoMap.get(roomId);
        UserInfoVo userInfoVo = userInfoVoMap.get(currentUsername);

        int countOnline = getCountOnline(roomId);
        MessageDTO messageDTO = GsonUtil.strToJavaBean(message, MessageDTO.class);
        String messageText = messageDTO.getMessage();
        String messageVoJson = MessageUtils.getMessageVo(false, userInfoVo, messageText, countOnline, null);
        broadcastAllUsers(messageVoJson, roomId);
        //添加聊天记录
        saveRoomChatRecord(roomId, messageText, userInfoVo);
    }

    /**
     * 保存聊天记录
     *
     * @param roomId
     * @param messageText
     * @param userInfoVo
     */
    private void saveRoomChatRecord(String roomId, String messageText, UserInfoVo userInfoVo) {
        Deque<RoomChatRecordVo> roomChatRecordVos = roomChatRecordMap.computeIfAbsent(roomId, k -> new ArrayDeque<>());
        if (roomChatRecordVos.size() > CHAT_RECORD_MAX) {
            //超过最大聊天记录数则移除队头消息
            roomChatRecordVos.poll();
        }
        RoomChatRecordVo roomChatRecordVo = new RoomChatRecordVo();
        roomChatRecordVo.setId(IdUtil.randomUUID().toString());
        roomChatRecordVo.setRoomId(roomId);
        roomChatRecordVo.setCreateTime(new Date(System.currentTimeMillis()));
        roomChatRecordVo.setMessage(messageText);
        roomChatRecordVo.setUserId(userInfoVo.getId());
        roomChatRecordVo.setNickname(userInfoVo.getNickname());
        roomChatRecordVo.setHeadUrl(userInfoVo.getHeadUrl());
        roomChatRecordVo.setUsername(userInfoVo.getUsername());
        roomChatRecordVos.offer(roomChatRecordVo);
        log.info("收到一条消息:{}", roomChatRecordVo);
        //每当内存中存储10条消息，存入redis中
        if (roomChatRecordVos.size() % 10 == 0) {
            RedisCache redisCache = SpringUtil.getBean(RedisCache.class);
            String recordsJson = GsonUtil.toJsonString(roomChatRecordVos);
            redisCache.setCacheMapValue(RedisRoomChatRecordKey.getRoomChatRecord, "", roomId, recordsJson);
        }
    }

    /**
     * 建立连接调用的方法，群成员加入
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") String roomId, @PathParam("authorization") String authorization) {
        RedisCache redisCache = SpringUtil.getBean(RedisCache.class);
        System.out.println("连接成功");
        Map<String, Session> sessionMap = roomSessionMap.computeIfAbsent(roomId, k -> new ConcurrentHashMap<>(10));
        //获取用户名
        String username = JwtUtil.getUsername(authorization);
        //将当前session存入房间
        sessionMap.put(username, session);
        AuthUser authUser = redisCache.getCacheObject(RedisUserKey.getUserInfo, username);
        User user = authUser.getUser();
        UserInfoVo userInfoVo = new UserInfoVo();
        if (user != null) {
            BeanUtils.copyProperties(user, userInfoVo);
        }
        //存入用户信息
        Map<String, UserInfoVo> userInfoVoMap = roomUserInfoMap.computeIfAbsent(roomId, k -> new ConcurrentHashMap<>(10));
        userInfoVoMap.put(username, userInfoVo);
        int countOnline = getCountOnline(roomId);
        //获取聊天记录
        Deque<RoomChatRecordVo> roomChatRecordVos = roomChatRecordMap.computeIfAbsent(roomId, k -> new ArrayDeque<>());
        //获取用户列表
        List<UserInfoVo> userInfoVoList = new ArrayList<>(userInfoVoMap.values());
        String onlineMessage = getOnlineMessage(userInfoVo.getNickname());
        String message = MessageUtils.getMessageVo(true, null, onlineMessage, countOnline, userInfoVoList);

        // 发送上线通知
        broadcastAllUsers(message, roomId);
        log.info("roomSessionMap:{},roomUserInfoMap:{}", roomSessionMap.toString(), roomUserInfoMap.toString());
    }

    /**
     * 用户上线系统消息
     *
     * @param nickname
     * @return
     */
    public String getOnlineMessage(String nickname) {
        return "用户{".concat(nickname).concat("}加入房间");
    }

    /**
     * 要将该消息推送给房间所有的客户端
     *
     * @param message
     */
    private void broadcastAllUsers(String message, String roomId) {
        Map<String, Session> sessionMap = roomSessionMap.get(roomId);
        Set<String> usernames = sessionMap.keySet();
        for (String username : usernames) {
            Session session = sessionMap.get(username);
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("send message error:{}", e);
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }

    /**
     * 获取在线人数
     *
     * @return
     */
    private int getCountOnline(String roomId) {
        Map<String, Session> sessionMap = roomSessionMap.computeIfAbsent(roomId, k -> new ConcurrentHashMap<>(10));
        Set<String> keySet = sessionMap.keySet();
        int count = 0;
        //在线人数
        if (!keySet.isEmpty()) {
            count = keySet.size();
        }
        return count;
    }


    /**
     * 关闭连接调用的方法，群成员退出
     *
     * @param session
     * @param roomId
     */
    @OnClose
    public void onClose(Session session, @PathParam("roomId") String roomId, @PathParam("authorization") String authorization) {
        System.out.println("连接断开");
        Map<String, Session> sessionMap = roomSessionMap.get(roomId);
        String username = JwtUtil.getUsername(authorization);
        sessionMap.remove(username);
        Map<String, UserInfoVo> userInfoVoMap = roomUserInfoMap.get(roomId);
        if (userInfoVoMap != null) {
            UserInfoVo userInfoVo = userInfoVoMap.remove(username);
            int countOnline = getCountOnline(roomId);
            if (userInfoVo != null) {
                List<UserInfoVo> userInfoVoList = new ArrayList<>(userInfoVoMap.values());
                // 发送离线通知
                String message = MessageUtils.getMessageVo(true, null, getOutlineMessage(userInfoVo.getNickname()), countOnline,userInfoVoList);
                broadcastAllUsers(message, roomId);
            }
        }
    }

    public String getOutlineMessage(String nickname) {
        return "用户{".concat(nickname).concat("}离开房间");
    }

    /**
     * 传输消息错误调用的方法
     *
     * @param error
     */
    @OnError
    public void OnError(Throwable error) {
        log.info("Connection error");
    }
}