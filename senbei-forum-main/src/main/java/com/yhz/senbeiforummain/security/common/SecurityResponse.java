package com.yhz.senbeiforummain.security.common;

import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.senbeiforummain.security.enums.SecurityErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 吉良吉影
 */
@Data
public class SecurityResponse <T> implements Serializable {
    private static final long serialVersionUID = 6178749053124050143L;
    private int code;

    private T data;

    private String message;

    private String description;

    public SecurityResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public SecurityResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    public SecurityResponse(int code, T data) {
        this(code, data, "", "");
    }

    public SecurityResponse(SecurityErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }
}