package com.yhz.senbeiforummain.common.annotation.resolver;

import com.yhz.senbeiforummain.common.annotation.UserId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yanhuanzhan
 * @date 2023/2/28 - 17:06
 */
public class UserIdResolver implements HandlerMethodArgumentResolver {
    @Value("${jwt.tokenHeader}")
    private String header;
    /**
     * 判断是否是注解类，返回true则调用resolveArgument方法
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(UserId.class);
    }
    /**
     * 对注解进行业务处理
     * @param methodParameter
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        //
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        Long userId = (Long) request.getAttribute("userId");
        //经过MyOncePerRequestFilter过滤器后，token校验成功，则将用户id放在请求中
        return userId;
    }

}
