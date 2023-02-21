package com.yhz.senbeiforummain.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.constant.SortConstant;
import com.yhz.commonutil.util.ImgUrlUtil;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.mapper.ModuleMapper;
import com.yhz.senbeiforummain.mapper.TopicMapper;
import com.yhz.senbeiforummain.mapper.UserMapper;
import com.yhz.senbeiforummain.model.dto.moduleconcern.ModuleConcernAddRequest;
import com.yhz.senbeiforummain.model.dto.moduleconcern.ModuleConcernQueryRequest;
import com.yhz.senbeiforummain.model.dto.moduleconcern.ModuleConcernRemoveRequest;
import com.yhz.senbeiforummain.model.entity.Module;
import com.yhz.senbeiforummain.model.entity.ModuleConcern;
import com.yhz.senbeiforummain.model.entity.Topic;
import com.yhz.senbeiforummain.model.entity.User;
import com.yhz.senbeiforummain.model.to.ModuleConcernTopicTo;
import com.yhz.senbeiforummain.model.vo.ModuleConcernTopicVo;
import com.yhz.senbeiforummain.service.IModuleConcernService;
import com.yhz.senbeiforummain.mapper.ModuleConcernMapper;
import com.yhz.senbeiforummain.util.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 吉良吉影
 * @description 针对表【module_concern(模块关注表)】的数据库操作Service实现
 * @createDate 2023-02-20 13:09:54
 */
@Service
public class IModuleConcernServiceImpl extends ServiceImpl<ModuleConcernMapper, ModuleConcern>
        implements IModuleConcernService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private ModuleMapper moduleMapper;
    @Resource
    private ModuleConcernMapper moduleConcernMapper;
    @Resource
    private TopicMapper topicMapper;
    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public boolean addModuleConcern(ModuleConcernAddRequest moduleConcernAddRequest) {
        Long userId = moduleConcernAddRequest.getUserId();
        Long moduleId = moduleConcernAddRequest.getModuleId();
        if (userId == null || moduleId == null || userId <= 0 || moduleId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //检查用户,模块是否存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userId);
        User user = userMapper.selectOne(userQueryWrapper);
        Optional.ofNullable(user).orElseThrow(() -> new BusinessException(ErrorCode.PARAMS_ERROR));
        QueryWrapper<Module> moduleQueryWrapper = new QueryWrapper<>();
        moduleQueryWrapper.eq("id", moduleId);
        Module module = moduleMapper.selectOne(moduleQueryWrapper);
        Optional.ofNullable(module).orElseThrow(() -> new BusinessException(ErrorCode.PARAMS_ERROR));
        //检查模块关注表中，两者的关注关系是否已经存在
        QueryWrapper<ModuleConcern> concernQueryWrapper = new QueryWrapper<>();
        concernQueryWrapper.eq("module_id", moduleId);
        concernQueryWrapper.eq("user_id", userId);
        ModuleConcern moduleConcern = this.baseMapper.selectOne(concernQueryWrapper);
        if (!ObjectUtil.isEmpty(moduleConcern)) {
            return true;
        }
        ModuleConcern newModuleConcern = new ModuleConcern();
        BeanUtils.copyProperties(moduleConcernAddRequest, newModuleConcern);
        int insert = this.baseMapper.insert(newModuleConcern);
        if (insert > 0) {
            //给模块增加关注数
            module.setConcernNum(module.getConcernNum() + 1);
            moduleMapper.updateById(module);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public IPage<ModuleConcernTopicVo> getModuleConcernTopicByPage(ModuleConcernQueryRequest moduleConcernQueryRequest) {
        long current = moduleConcernQueryRequest.getCurrent();
        long pageSize = moduleConcernQueryRequest.getPageSize();
        Long userId = moduleConcernQueryRequest.getUserId();
        String sortField = moduleConcernQueryRequest.getSortField();
        String sortOrder = moduleConcernQueryRequest.getSortOrder();

        //校验排序参数，防止sql注入，默认按时间降序排序
        if (StrUtil.isBlank(sortField) ||
                !StrUtil.equalsAny(sortField, "id", "reply_num", "click_num",
                        "support_num", "unsupport_num", "heat")) {
            //不符合上述条件，默认按时间排序
            sortField = "create_time";
        }
        if (StrUtil.isBlank(sortOrder) || !StrUtil.equalsAny(sortOrder, SortConstant.SORT_ORDER_ASC, SortConstant.SORT_ORDER_DESC)) {
            //默认按降序排序
            sortOrder = SortConstant.SORT_ORDER_DESC;
        }
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<ModuleConcern> moduleConcernQueryWrapper = new QueryWrapper<>();
        moduleConcernQueryWrapper.eq("user_id", userId);
        List<ModuleConcern> moduleConcernList = this.baseMapper.selectList(moduleConcernQueryWrapper);
        if (moduleConcernList.size() <= 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        List<Long> moduleIdList = moduleConcernList.stream().
                map(item -> item.getModuleId()).collect(Collectors.toList());

        IPage<Topic> topicIPage = PageUtil.vaildPageParam(current, pageSize);
        IPage<ModuleConcernTopicTo> topicToPage = topicMapper.selectModuleConcernToPage(topicIPage, moduleIdList, sortField, sortOrder);
        IPage<ModuleConcernTopicVo> topicVoIPage = topicToPage.convert(item -> {
            String imgUrls = item.getImgUrls();
            String[] imgUrlJsonToArray = ImgUrlUtil.imgUrlJsonToArray(imgUrls);
            ModuleConcernTopicVo moduleConcernTopicVo = new ModuleConcernTopicVo();
            BeanUtils.copyProperties(item, moduleConcernTopicVo);
            moduleConcernTopicVo.setImgUrlArray(imgUrlJsonToArray);
            return moduleConcernTopicVo;
        });
        return topicVoIPage;
    }



    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public void cancelModuleConcern(ModuleConcernRemoveRequest moduleConcernRemoveRequest) {
        Long userId = moduleConcernRemoveRequest.getUserId();
        Long moduleId = moduleConcernRemoveRequest.getModuleId();
        if (userId == null || moduleId == null || userId <= 0 || moduleId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        ModuleConcern newModuleConcern = new ModuleConcern();

        BeanUtils.copyProperties(moduleConcernRemoveRequest, newModuleConcern);
        QueryWrapper<ModuleConcern> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("module_id", moduleId);
        boolean remove = this.remove(wrapper);
        if (!remove) {
            throw new BusinessException(ErrorCode.DELETE_ERROR);
        }
        //模块关注数减1

        Module module = moduleMapper.selectById(moduleId);
        module.setConcernNum(module.getConcernNum() - 1);
        int result = moduleMapper.updateById(module);
        if (result <= 0) {
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
    }


}




