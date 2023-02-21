package com.yhz.senbeiforummain.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.constant.SortConstant;
import com.yhz.senbeiforummain.constant.ConcernConstant;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.mapper.ModuleConcernMapper;
import com.yhz.senbeiforummain.mapper.TopicMapper;
import com.yhz.senbeiforummain.model.dto.module.ModuleQueryRequest;
import com.yhz.senbeiforummain.model.entity.Module;
import com.yhz.senbeiforummain.model.entity.ModuleConcern;
import com.yhz.senbeiforummain.model.vo.ModuleVo;
import com.yhz.senbeiforummain.service.IModuleService;
import com.yhz.senbeiforummain.mapper.ModuleMapper;
import com.yhz.senbeiforummain.util.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * @author 吉良吉影
 * @description 针对表【module(模块表)】的数据库操作Service实现
 * @createDate 2022-11-13 16:08:53
 */
@Service
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, Module>
        implements IModuleService {
    @Resource
    private ModuleConcernMapper moduleConcernMapper;
    @Resource
    private TopicMapper topicMapper;
    @Override
    public IPage<ModuleVo> pageList(ModuleQueryRequest moduleQueryRequest) {
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
            } else if (sortOrder.equals(SortConstant.SORT_ORDER_ASC)) {
                wrapper.orderByAsc(sortField);
            } else {
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

    @Override
    public ModuleVo getModuleVo(Long moduleId, Long userId) {
        Optional.ofNullable(moduleId).orElseThrow(() -> new BusinessException(ErrorCode.PARAMS_ERROR));
        Module module = this.getById(moduleId);
        Optional.ofNullable(module).orElseThrow(() -> new BusinessException(ErrorCode.PARAMS_ERROR));
        ModuleVo moduleVo = new ModuleVo();
        BeanUtils.copyProperties(module, moduleVo);
        //判断是否关注
        if (userId != null && userId > 0) {
            QueryWrapper<ModuleConcern> moduleConcernQueryWrapper = new QueryWrapper<>();
            moduleConcernQueryWrapper.eq("module_id", moduleId);
            moduleConcernQueryWrapper.eq("user_id", userId);
            ModuleConcern moduleConcern = moduleConcernMapper.selectOne(moduleConcernQueryWrapper);
            if (!ObjectUtil.isEmpty(moduleConcern)) {
                moduleVo.setIsConcern(ConcernConstant.CONCERN);
            } else {
                moduleVo.setIsConcern(ConcernConstant.NOT_CONCERN);
            }
        }
        return moduleVo;
    }
    @Override
    public int calibrationConcern() {
        List<Module> moduleList = this.baseMapper.selectList(null);
        List<Long> moduleIdList = moduleList.stream().map(module -> module.getId()).collect(Collectors.toList());
        List<Module> newModuleList=moduleConcernMapper.getConcernNumByModuleIdList(moduleIdList);
        int res=this.baseMapper.updateBatchById(newModuleList);
        return res;
    }

    @Override
    public int calibrationTopicNum() {
        List<Module> moduleList = this.baseMapper.selectList(null);
        List<Long> moduleIdList = moduleList.stream().map(module -> module.getId()).collect(Collectors.toList());
        List<Module> newModuleList=topicMapper.getTopicNumByModuleIdList(moduleIdList);
        int res = this.baseMapper.updateBatchById(newModuleList);
        return res;
    }
}



