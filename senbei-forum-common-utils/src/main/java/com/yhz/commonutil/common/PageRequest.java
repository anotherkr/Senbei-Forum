package com.yhz.commonutil.common;

import com.yhz.commonutil.constant.SortConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页请求
 *
 * @author yupi
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private long current;

    /**
     * 页面大小
     */
    private long pageSize;

    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    @ApiModelProperty("排序顺序(ascend-升序,descend-降序),默认升序")
    private String sortOrder = SortConstant.SORT_ORDER_ASC;
}
