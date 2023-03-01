package com.yhz.senbeiforummain.model.dto.moduleconcern;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanhuanzhan
 * @date 2023/2/20 - 13:57
 */
@Data
public class ModuleConcernRemoveRequest {
    /**
     * 模块id
     */
    @ApiModelProperty("模块id")
    private Long moduleId;


}
