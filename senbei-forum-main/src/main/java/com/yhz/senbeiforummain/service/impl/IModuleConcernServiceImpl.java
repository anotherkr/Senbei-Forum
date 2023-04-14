package com.yhz.senbeiforummain.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.constant.SortConstant;
import com.yhz.commonutil.util.ImgUrlUtil;
import com.yhz.senbeiforummain.constant.ConcernConstant;
import com.yhz.senbeiforummain.constant.rediskey.RedisTopicKey;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.mapper.ModuleMapper;
import com.yhz.senbeiforummain.mapper.TopicMapper;
import com.yhz.senbeiforummain.mapper.UserMapper;
import com.yhz.senbeiforummain.model.dto.moduleconcern.ModuleConcernAddRequest;
import com.yhz.senbeiforummain.model.dto.moduleconcern.ModuleConcernQueryRequest;
import com.yhz.senbeiforummain.model.entity.Module;
import com.yhz.senbeiforummain.model.entity.ModuleConcern;
import com.yhz.senbeiforummain.model.entity.Topic;
import com.yhz.senbeiforummain.model.entity.User;
import com.yhz.senbeiforummain.model.enums.SupportEnum;
import com.yhz.senbeiforummain.model.to.ModuleConcernTopicTo;
import com.yhz.senbeiforummain.model.vo.ModuleConcernTopicVo;
import com.yhz.senbeiforummain.service.IModuleConcernService;
import com.yhz.senbeiforummain.mapper.ModuleConcernMapper;
import com.yhz.senbeiforummain.util.PageUtil;
import com.yhz.senbeiforummain.util.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
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
    private TopicMapper topicMapper;
    @Resource
    private RedisCache redisCache;

    @Override
    public IPage<ModuleConcernTopicVo> getModuleConcernTopicByPage(ModuleConcernQueryRequest moduleConcernQueryRequest) {
        long current = moduleConcernQueryRequest.getCurrent();
        long pageSize = moduleConcernQueryRequest.getPageSize();
        Long userId = moduleConcernQueryRequest.getUserId();
        String sortField = moduleConcernQueryRequest.getSortField();
        String sortOrder = moduleConcernQueryRequest.getSortOrder();
        sortField = PageUtil.sqlInject(sortField);
        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<ModuleConcern> moduleConcernQueryWrapper = new QueryWrapper<>();
        moduleConcernQueryWrapper.eq("user_id", userId);
        moduleConcernQueryWrapper.eq("is_concern", ConcernConstant.CONCERN);
        List<ModuleConcern> moduleConcernList = this.baseMapper.selectList(moduleConcernQueryWrapper);
        if (moduleConcernList.size() <= 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        List<Long> moduleIdList = moduleConcernList.stream().
                map(item -> item.getModuleId()).collect(Collectors.toList());

        IPage<Topic> topicIPage = PageUtil.vaildPageParam(current, pageSize);
        IPage<ModuleConcernTopicTo> topicToPage = topicMapper.selectModuleConcernToPage(topicIPage, moduleIdList, sortField, sortOrder);
        //从redis中获取缓存信息
        Map<String, Integer> supportInfoCacheMap = redisCache.getCacheMap(RedisTopicKey.getSupportInfo, "");
        Map<String, Integer> supportCountCacheMap = redisCache.getCacheMap(RedisTopicKey.getSupportCount, "");
        IPage<ModuleConcernTopicVo> topicVoIPage = topicToPage.convert(item -> {
            Long topicId = item.getId();
            String imgUrls = item.getImgUrls();
            String[] imgUrlJsonToArray = ImgUrlUtil.imgUrlJsonToArray(imgUrls);
            ModuleConcernTopicVo moduleConcernTopicVo = new ModuleConcernTopicVo();
            BeanUtils.copyProperties(item, moduleConcernTopicVo);
            moduleConcernTopicVo.setImgUrlArray(imgUrlJsonToArray);
            //判断用户是否点赞
            if (supportInfoCacheMap != null) {
                String hKey = topicId.toString().concat("::").concat(userId.toString());
                Integer isSupport = supportInfoCacheMap.getOrDefault(hKey,0);
                moduleConcernTopicVo.setIsSupport(isSupport);
            } else {
                moduleConcernTopicVo.setIsSupport(SupportEnum.NO_SUPPORT.getCode());
            }
            //将缓存中的点赞数加入主贴信息
            if (supportCountCacheMap != null) {
                Integer supportNum = Optional.ofNullable(supportCountCacheMap.get(topicId)).orElse(SupportEnum.NO_SUPPORT.getCode());
                moduleConcernTopicVo.setSupportNum(moduleConcernTopicVo.getSupportNum() + supportNum);

            }
            return moduleConcernTopicVo;
        });
        return topicVoIPage;
    }


    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public void concernModule(Long moduleId, Long userId, Integer concern) {
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
            moduleConcern.setIsConcern(concern);
            boolean update = this.updateById(moduleConcern);
            if (!update) {
                throw new BusinessException(ErrorCode.UPDATE_ERROR);
            }
        } else {
            moduleConcern = new ModuleConcern();
            moduleConcern.setModuleId(moduleId);
            moduleConcern.setUserId(userId);
            moduleConcern.setIsConcern(concern);
            boolean save = this.save(moduleConcern);
            if (!save) {
                throw new BusinessException(ErrorCode.SAVE_ERROR);
            }
        }
        //修改模块关注数
        //模块关注数加减
        updateModuleConcernNum(module, concern);
    }

    private void updateModuleConcernNum(Module module, Integer concern) {
        if (ConcernConstant.CONCERN.equals(concern)) {
            module.setConcernNum(module.getConcernNum() + 1);
        } else if (ConcernConstant.NOT_CONCERN.equals(concern)) {
            module.setConcernNum(module.getConcernNum() - 1);
        }
        int result = moduleMapper.updateById(module);
        if (result <= 0) {
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
    }

}




