package com.yhz.senbeiforummain.handler;

import com.yhz.senbeiforummain.common.enums.LoginChannelEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录功能处理器接口
 * @author yanhuanzhan
 * @date 2022/12/14 - 23:34
 */
public interface OauthLoginHandler {
    /**
     * 获取登录渠道
     * @return
     */
    LoginChannelEnum getChannel();

    /**
     * 处理登录
     * @param code 授权码
     * @param response
     */
    void dealLogin(String code, HttpServletResponse response) throws IOException;

    /**
     *  拼接获取授权码的URL
     * @return
     */
    String getUrl();
}
