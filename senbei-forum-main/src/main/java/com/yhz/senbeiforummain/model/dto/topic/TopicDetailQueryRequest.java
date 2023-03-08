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
public class TopicDetailQueryRequest extends PageRequest {
    @NotNull(message = "主貼id不能爲空")
    private Long topicId;
    @ApiModelProperty("用戶id，使用只看樓主功能時使用")
    private Long userId;
}
