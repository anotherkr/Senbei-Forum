package com.yhz.senbeiforummain.model.enums;

/**
 * @author yanhuanzhan
 * @date 2022/12/5 - 16:22
 */
public enum SupportEnum {




    SUPPORT(1,"已点赞"),
    NO_SUPPORT(0,"未点赞");

    /**
     * 是否点赞(1-点赞 0-未点赞)
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String description;

    SupportEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
