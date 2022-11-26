package com.yhz.senbeiforummain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.senbeiforummain.constant.SortType;
import com.yhz.senbeiforummain.domain.dto.ModulePageDto;
import com.yhz.senbeiforummain.domain.Module;
import com.yhz.senbeiforummain.service.IModuleService;
import com.yhz.senbeiforummain.mapper.ModuleMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author 吉良吉影
* @description 针对表【module(模块表)】的数据库操作Service实现
* @createDate 2022-11-13 16:08:53
*/
@Service
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, Module>
    implements IModuleService {

    @Override
    public IPage<Module> pageList(ModulePageDto modulePageDto) {
        Integer currentPage = modulePageDto.getCurrentPage();
        Integer pageSize = modulePageDto.getPageSize();
        Integer sortType = modulePageDto.getSortType();
        IPage<Module> iPage = new Page<>(currentPage,pageSize);
        QueryWrapper<Module> wrapper = new QueryWrapper<>();
        String name = modulePageDto.getName();
        wrapper.like(!StringUtils.isEmpty(name),"name", name);
        //默认按热度排序
        if (sortType == null || sortType.equals(SortType.SORT_BY_HEAT)) {
            wrapper.orderByDesc("heat");
        } else if (sortType.equals(SortType.SORT_BY_TIME)) {
            wrapper.orderByDesc("create_time");
        }
        IPage<Module> page = this.page(iPage,wrapper);
        return page;
    }
}




