package com.yhz.senbeiforummain.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yhz.commonutil.constant.PageConstant;

import java.util.Optional;

/**
 * @author yanhuanzhan
 * @date 2023/2/19 - 18:11
 */
public class PageUtil {
    public static <T> IPage<T> vaildPageParam(long current, long pageSize) {
        long checkCurrent = Optional.ofNullable(current).filter(c->c>0).orElse(PageConstant.defaultCurrent);
        long checkPageSize = Optional.ofNullable(pageSize).filter(size->size>0).orElse(PageConstant.defaultPageSize);

        IPage<T> iPage = new Page<>(checkCurrent, checkPageSize);
        return iPage;
    }
}
