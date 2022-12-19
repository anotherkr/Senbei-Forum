package com.yhz.senbeiforummain.service;

import com.yhz.senbeiforummain.domain.oos.OssRequestParam;
import com.yhz.senbeiforummain.exception.BusinessException;

/**
 *  阿里云对象存储业务层
 * @author yanhuanzhan
 * @date 2022/11/21 - 12:43
 */
public interface IOssService {
    /**
     * 获取阿里云对象存储需要的参数
     * @return
     * @throws BusinessException
     */
     OssRequestParam policy() throws BusinessException;
}
