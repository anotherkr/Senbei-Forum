package com.yhz.senbeiforummain.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.constant.SortConstant;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.model.dto.module.ModuleQueryRequest;
import com.yhz.senbeiforummain.model.entity.Module;
import com.yhz.senbeiforummain.model.vo.ModuleVo;
import com.yhz.senbeiforummain.service.IModuleService;
import com.yhz.senbeiforummain.mapper.ModuleMapper;
import com.yhz.senbeiforummain.util.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author 吉良吉影
 * @description 针对表【module(模块表)】的数据库操作Service实现
 * @createDate 2022-11-13 16:08:53
 */
@Service
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, Module>
        implements IModuleService {

    @Override
    public IPage<ModuleVo> pageList(ModuleQueryRequest moduleQueryRequest) {
        Optional.ofNullable(moduleQueryRequest).orElseThrow(() -> new BusinessException(ErrorCode.PARAMS_ERROR));
        IPage<Module> iPage = PageUtil.vaildPageParam(moduleQueryRequest.getCurrent(), moduleQueryRequest.getPageSize());
        QueryWrapper<Module> wrapper = new QueryWrapper<>();
        Long id = moduleQueryRequest.getId();
        String name = moduleQueryRequest.getName();
        Long userId = moduleQueryRequest.getUserId();
        String sortField = moduleQueryRequest.getSortField();
        String sortOrder = moduleQueryRequest.getSortOrder();
        wrapper.eq(id != null, "id", id);
        wrapper.like(!StrUtil.isBlank(name), "name", name);
        wrapper.eq(userId != null, "user_id", userId);
        if (!StrUtil.isBlank(sortField)) {
            if (sortOrder.equals(SortConstant.SORT_ORDER_DESC)) {
                wrapper.orderByDesc(sortField);
            } else if(sortOrder.equals(SortConstant.SORT_ORDER_ASC)){
                wrapper.orderByAsc(sortField);
            }else {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        IPage<Module> page = this.page(iPage, wrapper);
        if (page.getRecords().size() == 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        IPage<ModuleVo> moduleVoIPage = page.convert(item -> {
            ModuleVo moduleVo = new ModuleVo();
            BeanUtils.copyProperties(item, moduleVo);
            return moduleVo;
        });
        return moduleVoIPage;
    }
}




