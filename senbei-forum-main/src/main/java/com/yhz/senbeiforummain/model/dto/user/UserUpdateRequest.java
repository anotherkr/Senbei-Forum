package com.yhz.senbeiforummain.model.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanhuanzhan
 * @date 2023/3/7 - 16:06
 */
@Data
public class UserUpdateRequest {
    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 个人描述
     */
    private String description;
    /**
     * 性别(0-未知,1-男,2-女人)
     */
    private Integer sex;

    /**
     * 用户头像
     */
    private String headUrl;
}
