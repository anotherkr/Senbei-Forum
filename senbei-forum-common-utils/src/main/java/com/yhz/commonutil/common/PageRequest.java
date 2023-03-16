package com.yhz.commonutil.common;

import cn.hutool.core.util.StrUtil;
import cn.hutool.script.ScriptUtil;
import com.yhz.commonutil.constant.PageConstant;
import com.yhz.commonutil.constant.SortConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 分页请求
 *
 * @author yupi
 */
public class PageRequest {

    /**
     * 当前页号
     */
    private Long current;

    /**
     * 页面大小
     */
    private Long pageSize;

    /**
     * 排序字段
     */
    @ApiModelProperty("排序字段")
    @NotBlank(message = "排序字段不能为空")
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    @ApiModelProperty("排序顺序(ascend-升序,descend-降序),默认升序")
    @NotBlank(message = "排序方式不能为空")
    private String sortOrder = SortConstant.SORT_ORDER_ASC;

    public Long getCurrent() {
        return current==null||current<1L?PageConstant.defaultCurrent:current;
    }

    public void setCurrent(Long current) {
        this.current = current;
    }

    public Long getPageSize() {
        return pageSize==null||pageSize<1L?PageConstant.defaultPageSize:pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortField() {
        return StrUtil.isBlank(sortField)?"create_time":sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return !StrUtil.equalsAny(sortOrder,SortConstant.SORT_ORDER_ASC,SortConstant.SORT_ORDER_DESC)?SortConstant.SORT_ORDER_DESC:sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
