package com.yhz.senbeiforummain.domain.vo;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yanhuanzhan
 * @date 2022/11/25 - 20:41
 */
@Data
@ApiModel(description = "用户基本信息")
public class UserInfoVo {
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long id;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * 账号
     */
    @ApiModelProperty("账号")
    private String username;
    /**
     * 积分
     */
    @ApiModelProperty("积分")
    private Long point;
    /**
     * 用户等级(1-6)
     */
    @ApiModelProperty("用户等级(1-6)")
    private Integer level;
    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    private Long roleId;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 个人描述
     */
    @ApiModelProperty("个人描述")
    private String description;
    /**
     * 性别(0-未知,1-男,2-女人)
     */
    @ApiModelProperty("性别(0-未知,1-男,2-女人)")
    private Integer sex;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    private String headUrl;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
}
