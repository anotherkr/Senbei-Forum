package com.yhz.senbeiforummain.exception;

import com.yhz.commonutil.ErrorCode;

/**
 * 自定义异常类
 * @author yanhuanzhan
 * @date 2022/11/13 - 14:02
 */
public class BusinessException extends RuntimeException {
    private final int code;

    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}