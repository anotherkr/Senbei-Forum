package com.yhz.senbeiforummain.security.handler;

import com.yhz.senbeiforummain.security.common.SecurityResponse;
import com.yhz.senbeiforummain.security.enums.SecurityErrorCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足处理器
 * @author 吉良吉影
 */
@Component
public class MyAccessDeniedHandler extends JSONAuthentication implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        //用户权限不足
        SecurityResponse securityResponse = new SecurityResponse(SecurityErrorCode.USER_NO_PERMISSIONS);
        //输出
        this.WriteJSON(request, response, securityResponse);

    }
}
