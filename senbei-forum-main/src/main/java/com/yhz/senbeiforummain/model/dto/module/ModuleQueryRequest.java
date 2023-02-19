package com.yhz.senbeiforummain.model.dto.module;

import com.yhz.commonutil.common.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanhuanzhan
 * @date 2022/11/13 - 18:09
 */
@Data
@ApiModel(description = "模块分页请求参数")
public class ModuleQueryRequest extends PageRequest {
    private Long id;

    /**
     * 模块名
     */
    @ApiModelProperty("模块名")
    private String name;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;
}
