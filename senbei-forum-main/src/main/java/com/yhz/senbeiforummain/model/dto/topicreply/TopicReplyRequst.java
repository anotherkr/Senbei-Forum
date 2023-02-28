package com.yhz.senbeiforummain.model.dto.topicreply;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author yanhuanzhan
 * @date 2022/11/27 - 2:56
 */
@Data
@ApiModel("主贴回复请求参数")
public class TopicReplyRequst {
    /**
     * 主贴id
     */
    @ApiModelProperty("主贴id")
    @NotNull(message = "主贴id不能为空")
    private Long topicId;

    /**
     * 回复内容
     */
    @ApiModelProperty("回复内容(5-2000字)")
    @Size(min = 3,max = 2001,message = "回复内容应为5-2000字之间")
    private String content;

    @ApiModelProperty("图片地址")
    private String[] imgUrlArray;

}
