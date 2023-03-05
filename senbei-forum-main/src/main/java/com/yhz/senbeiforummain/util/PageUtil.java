package com.yhz.senbeiforummain.util;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.constant.PageConstant;
import com.yhz.commonutil.constant.SortConstant;
import com.yhz.senbeiforummain.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;


import java.util.Optional;

/**
 * @author yanhuanzhan
 * @date 2023/2/19 - 18:11
 */
public class PageUtil {
    /**
     * 校验current和pagesize，如果不符合条件则赋上默认值并创建ipage返回
     * @param current
     * @param pageSize
     * @param <T>
     * @return
     */
    public static <T> IPage<T> vaildPageParam(long current, long pageSize) {
        long checkCurrent = Optional.ofNullable(current).filter(c->c>0).orElse(PageConstant.defaultCurrent);
        long checkPageSize = Optional.ofNullable(pageSize).filter(size->size>0).orElse(PageConstant.defaultPageSize);
        IPage<T> iPage = new Page<>(checkCurrent, checkPageSize);
        return iPage;
    }

    /**
     * 处理QueryWrapper的排序
     * @param wrapper
     * @param sortField
     * @param sortOrder
     * @param <T>
     * @return
     */
    public static <T> void dealSortWrapper(QueryWrapper<T> wrapper, String sortField, String sortOrder) {
        if (!StrUtil.isBlank(sortField)) {
            //防止sql注入
            String checkSortField = sqlInject(sortField);
            if (sortOrder.equals(SortConstant.SORT_ORDER_DESC)) {
                wrapper.orderByDesc(checkSortField);
            } else if(sortOrder.equals(SortConstant.SORT_ORDER_ASC)){
                wrapper.orderByAsc(checkSortField);
            }else {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
    }
    /**
     * SQL注入过滤
     * @param str  待验证的字符串
     * @throws
     */
    public static String sqlInject(String str){
        if(StringUtils.isBlank(str)){
            return null;
        }
        //去掉'|"|;|\字符
        str = StringUtils.replace(str, "'", "");
        str = StringUtils.replace(str, "\"", "");
        str = StringUtils.replace(str, ";", "");
        str = StringUtils.replace(str, "\\", "");

        //转换成小写
        str = str.toLowerCase();

        //非法字符
        String[] keywords = {"master", "truncate", "insert", "select", "delete", "update", "declare", "alert", "drop"};

        //判断是否包含非法字符
        for(String keyword : keywords){
            if(str.indexOf(keyword) != -1){
                throw new BusinessException(ErrorCode.SQL_ERROR);
            }
        }

        return str;
    }


}
