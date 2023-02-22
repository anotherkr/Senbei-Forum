package com.yhz.senbeiforummain.model.vo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * 图片验证码
 * @author yanhuanzhan
 * @date 2023/2/21 - 22:04
 */
@Data
public class CaptchaImageVo {
    @ApiModelProperty("uuid,在登录时需同验证码一起上传")
    private String uuid;
    @ApiModelProperty("验证码图片")
    private String imgBase64;
}
