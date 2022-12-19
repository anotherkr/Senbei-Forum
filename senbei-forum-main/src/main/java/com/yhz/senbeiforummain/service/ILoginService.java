package com.yhz.senbeiforummain.service;

import com.yhz.senbeiforummain.domain.dto.EmailRegisterDto;
import com.yhz.senbeiforummain.domain.dto.LoginDto;
import com.yhz.senbeiforummain.common.enums.LoginChannelEnum;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 吉良吉影
 */
public interface ILoginService {
    /**
     * 用户登录
     * @param loginDTO
     * @return
     */
    String doLogin(LoginDto loginDTO);

    /**
     * 用户注销
     */
    void doLogout();

    /**
     * 向邮箱发送验证码
     * @param email
     */
    void mailSend(String email);

    /**
     * 邮箱注册
     * @param registerDto
     */
    void emailRegister(EmailRegisterDto registerDto);

    /**
     *  获取前端跳转的url，跳转并授权后获取授权码
     * @return
     * @param loginChannelEnum 用来指定第三登录的渠道
     */
    String getOauthUrl(LoginChannelEnum loginChannelEnum);

    /**
     * oauth 认证登录
     * @param loginChannelEnum 登录方式枚举类
     * @param code 授权码
     * @param response
     */
    void oauthLogin(LoginChannelEnum loginChannelEnum, String code, HttpServletResponse response) throws IOException;
}
