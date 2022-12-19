package com.yhz.senbeiforummain.security.handler;

import com.yhz.commonutil.common.BaseResponse;
import com.yhz.senbeiforummain.security.common.SecurityResponse;
import com.yhz.senbeiforummain.security.enums.SecurityErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 *
 * @author 吉良吉影
 */
@Component
public class MyAuthenticationEntryPoint extends JSONAuthentication implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(MyAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //打印错误
        logger.error("Authentication error:{}", authException);
        //用户未登录或者身份校验失败

        SecurityResponse securityResponse;
        if (authException instanceof AccountExpiredException) {
            //账号过期
            securityResponse = new SecurityResponse(SecurityErrorCode.USER_ACCOUNT_EXPIRED);
        } else if (authException instanceof InternalAuthenticationServiceException) {
            //用户不存在
            securityResponse = new SecurityResponse(SecurityErrorCode.USER_ACCOUNT_NOT_EXIST);
        } else if (authException instanceof BadCredentialsException) {
            //用户名或密码错误（也就是用户名匹配不上密码）
            securityResponse = new SecurityResponse(SecurityErrorCode.USERNAME_PASSWORD_ERROR);
        } else if (authException instanceof CredentialsExpiredException) {
            //密码过期
            securityResponse = new SecurityResponse(SecurityErrorCode.USER_PASSWORD_EXPIRED);
        } else if (authException instanceof DisabledException) {
            //账号不可用
            securityResponse = new SecurityResponse(SecurityErrorCode.USER_ACCOUNT_DISABLE);
        } else if (authException instanceof LockedException) {
            //账号锁定
            securityResponse = new SecurityResponse(SecurityErrorCode.USER_ACCOUNT_LOCKED);
        } else {
            //其他错误
            securityResponse = new SecurityResponse(SecurityErrorCode.USER_NOT_LOGIN);
        }

        this.WriteJSON(request, response, securityResponse);

    }
}
