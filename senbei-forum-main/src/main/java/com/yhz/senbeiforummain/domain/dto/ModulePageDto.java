package com.yhz.senbeiforummain.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanhuanzhan
 * @date 2022/11/13 - 18:09
 */
@Data
@ApiModel(description = "模块分页请求参数")
public class ModulePageDto {
    /**
     * 当前页码
     */
    @ApiModelProperty("当前页码")
    public Integer currentPage;
    /**
     * 每页大小
     */
    @ApiModelProperty("每页大小")
    public Integer pageSize;
    /**
     * 模块名
     */
    @ApiModelProperty("模块名(模糊查询)")
    public String name;
    /**
     * 排序方式(0-按热度排序，1-按时间排序)
     */
    @ApiModelProperty("排序方式(0-按热度排序，1-按时间排序)")
    public Integer sortType;
}
