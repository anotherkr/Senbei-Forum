package com.yhz.senbeiforummain.exception;

import com.yhz.commonutil.BaseResponse;
import com.yhz.commonutil.ErrorCode;
import com.yhz.commonutil.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    @ResponseBody
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("--------------businessException: " + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public BaseResponse runtimeExceptionHandler(RuntimeException e) {
        log.error("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
