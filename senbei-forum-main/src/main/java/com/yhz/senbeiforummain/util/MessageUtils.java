package com.yhz.senbeiforummain.util;

import com.yhz.senbeiforummain.model.vo.MessagegVO;
import com.yhz.senbeiforummain.model.vo.RoomChatRecordVo;
import com.yhz.senbeiforummain.model.vo.UserInfoVo;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author yanhuanzhan
 * @date 2023/3/20 - 1:40
 */
public class MessageUtils {
    public static String getMessageVo(boolean isSystemMessage, UserInfoVo userInfoVo, Object message, int count, List<UserInfoVo> userInfoVoList) {
        MessagegVO messagegVO = new MessagegVO();
        if (userInfoVo != null) {
            BeanUtils.copyProperties(userInfoVo,messagegVO);
        }
        messagegVO.setIsSystem(isSystemMessage);
        messagegVO.setMessage(message);
        messagegVO.setCount(count);
        messagegVO.setUserInfoVoList(userInfoVoList);
        String messageVoJson = GsonUtil.toJsonString(messagegVO);
        return messageVoJson;
    }
}
