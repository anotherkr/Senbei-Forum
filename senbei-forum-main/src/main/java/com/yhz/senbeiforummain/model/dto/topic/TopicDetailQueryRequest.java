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
    private Long topicId;
}
