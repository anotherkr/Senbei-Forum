package com.yhz.senbeiforummain.model.dto.topic;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author yanhuanzhan
 * @date 2022/11/26 - 18:55
 */
@Data
@Api("发布主贴接口需要的请求参数")
public class TopicAddRequst {
    /**
     * 模块id
     */
    @ApiModelProperty("模块id")
    @NotNull(message = "模块id不能为空")
    private Long moduleId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @NotNull(message = "用户id不能为空")
    private Long userId;
    /**
     * 主贴标题
     */
    @ApiModelProperty("主贴标题(5-30字)")
    @Size(min = 5,max = 2000,message = "标题需要在5-30字之间")
    private String title;
    /**
     * 主贴内容
     */
    @ApiModelProperty("主贴内容(5-2000字)")
    @Size(min = 5,max = 2000,message = "内容需要在5-2000字之间")
    private String content;
    /**
     * 图片地址(0-3张)
     */
    @ApiModelProperty("图片地址(0-3张)")
    private String[] imgUrlArray;


}
