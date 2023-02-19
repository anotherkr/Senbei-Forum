package com.yhz.senbeiforummain.model.dto.topic;

import com.yhz.commonutil.common.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author yanhuanzhan
 * @date 2022/11/25 - 15:45
 */
@Data
@ApiModel(description = "主帖分页请求参数")
public class TopicQueryRequest extends PageRequest {
    private Long id;
    /**
     * 主贴标题
     */
    @ApiModelProperty("主贴标题")
    private String title;
    /**
     * 模块id
     */
    @ApiModelProperty("模块id")
    @NotNull(message = "模块id不能为空")
    private Long moduleId;
    /**
     * 主贴内容(5-2000字)
     */
    @ApiModelProperty("主贴内容")
    private String content;
    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;
}
