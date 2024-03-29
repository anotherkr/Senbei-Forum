package com.yhz.commonutil.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @author yanhuanzhan
 * @date 2022/11/13 - 14:35
 */
@Data
public class BaseResponse <T> implements Serializable {
    private static final long serialVersionUID = 6178749053124050143L;
    private int code;

    private T data;

    private String message;

    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}
