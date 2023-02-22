package com.yhz.senbeiforummain.model.dto.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author yanhuanzhan
 * @date 2022/12/4 - 21:18
 */
@ApiModel("用户登录传输参数")
@Data
public class DoLoginRequest {
    @ApiModelProperty("用户名")
    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("验证码")
    private String captchaCode;
    @ApiModelProperty("uuid用于取验证码")
    private String uuid;
}
