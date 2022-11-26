package com.yhz.senbeiforummain.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yanhuanzhan
 * @date 2022/11/25 - 15:45
 */
@Data
@ApiModel(description = "主帖分页请求参数")
public class ModuleTopicPageDto {
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
     * 模块id
     */
    @ApiModelProperty("模块id")
    public Long moduleId;
    /**
     * 主贴标题(模糊查询)
     */
    @ApiModelProperty("主贴标题(模糊查询)")
    public String title;
    /**
     * 排序方式(0-按热度排序，1-按时间排序)
     */
    @ApiModelProperty("排序方式(0-按热度排序，1-按时间排序)")
    public Integer sortType;
}
