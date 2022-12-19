package com.yhz.senbeiforummain.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author yanhuanzhan
 * @date 2022/12/5 - 3:13
 */
@Data
@ApiModel("邮箱注册传输参数")
public class EmailRegisterDto {
    @ApiModelProperty("昵称(2-10字)")
    @Size(message = "昵称必须在2-10字以内",min = 2,max = 10)
    private String nickname;
    @ApiModelProperty("邮箱")
    @Email(message = "请输入正确的邮箱格式")
    private String email;
    @ApiModelProperty("密码")
    @Size(min = 3,max = 20)
    private String password;
    @Size(min = 3,max = 20)
    @ApiModelProperty("重复密码")
    private String checkPassword;
    @NotEmpty
    @ApiModelProperty("验证码")
    private String code;

}
