package com.yhz.senbeiforummain.model.dto.moduleconcern;

import com.yhz.commonutil.common.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @author yanhuanzhan
 * @date 2023/2/20 - 14:53
 */
@Data
@ApiOperation("获取关注模块对应帖子的请求")
public class ModuleConcernQueryRequest extends PageRequest {
    @ApiModelProperty("用户id")
    private Long userId;
}
