package com.yhz.senbeiforummain.exception;

import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 *  全局异常处理器
 * @author yanhuanzhan
 * @date 2022/11/13 - 14:01
 */
@RestControllerAdvice
@Slf4j
@Component
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("--------------businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public BaseResponse paramExceptionHandler(MethodArgumentNotValidException e) {
        BindingResult exceptions = e.getBindingResult();
        // 判断异常中是否有错误信息，如果存在就使用异常中的消息，否则使用默认消息
        if (exceptions.hasErrors()) {
            List<ObjectError> errors = exceptions.getAllErrors();
            if (!errors.isEmpty()) {
                // 这里列出了全部错误参数，按正常逻辑，只需要第一条错误即可
                FieldError fieldError = (FieldError) errors.get(0);
                return ResultUtils.error(400,fieldError.getDefaultMessage(),"");
            }
        }
        return ResultUtils.error(ErrorCode.PARAMS_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
    @ExceptionHandler(Exception.class)
    public BaseResponse ExceptionHandler(Exception e) {
        log.error("Exception", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
    /**
     * @Description: 将 AuthenticationException 异常往上抛，让认证处理器去处理
     */
    @ExceptionHandler(value = AuthenticationException.class)
    public void accountExpiredExceptionHandler(AuthenticationException authException){
        throw authException;
    }

    /**
     * 将 AccessDeniedException 异常往上抛，让授权处理器去处理
     * @param accDenException
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public void accessDeniedExceptionHandler(AccessDeniedException accDenException){
        throw accDenException;
    }

}
