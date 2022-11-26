package com.yhz.senbeiforummain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.ErrorCode;
import com.yhz.senbeiforummain.constant.SortType;
import com.yhz.senbeiforummain.domain.ModuleTopic;
import com.yhz.senbeiforummain.domain.User;
import com.yhz.senbeiforummain.domain.dto.ModuleTopicPageDto;
import com.yhz.senbeiforummain.domain.vo.ModuleTopicVo;
import com.yhz.senbeiforummain.domain.vo.UserInfoVo;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.mapper.UserMapper;
import com.yhz.senbeiforummain.service.IModuleTopicService;
import com.yhz.senbeiforummain.mapper.ModuleTopicMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 吉良吉影
* @description 针对表【module_topic(主贴表)】的数据库操作Service实现
* @createDate 2022-11-13 16:08:53
*/
@Service
public class ModuleTopicServiceImpl extends ServiceImpl<ModuleTopicMapper, ModuleTopic>
    implements IModuleTopicService {
    @Resource
    private UserMapper userMapper;
    @Override
    public IPage<ModuleTopicVo> pageList(ModuleTopicPageDto moduleTopicPageDto) throws BusinessException {
        Integer pageSize = moduleTopicPageDto.getPageSize();
        Integer currentPage = moduleTopicPageDto.getCurrentPage();
        Long moduleId = moduleTopicPageDto.getModuleId();
        String title = moduleTopicPageDto.getTitle();
        Integer sortType = moduleTopicPageDto.getSortType();
        if (pageSize == null || currentPage == null || moduleId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        IPage<ModuleTopic> page = new Page<>(currentPage,pageSize);
        QueryWrapper<ModuleTopic> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(title), "title", title);
        wrapper.eq("module_id",moduleId);
        //默认按热度排序
        if (sortType == null || sortType.equals(SortType.SORT_BY_HEAT)) {
            wrapper.orderByDesc("heat");
        } else if (sortType.equals(SortType.SORT_BY_TIME)) {
            wrapper.orderByDesc("create_time");
        }
        IPage<ModuleTopic> moduleTopicIPage = this.page(page, wrapper);
        //若查询到的数据为空，抛出异常
        if (moduleTopicIPage.getRecords().size()==0) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        List<User> userList = userMapper.selectList(null);
        //传入主贴对应的用户信息
        IPage<ModuleTopicVo> moduleTopicVoIPage = moduleTopicIPage.convert(moduleTopic -> {
            ModuleTopicVo moduleTopicVo = new ModuleTopicVo();
            BeanUtils.copyProperties(moduleTopic, moduleTopicVo);
            Long userId = moduleTopic.getUserId();
            userList.forEach(user -> {
                if (user.getId().equals(userId)) {
                    UserInfoVo userInfoVo = new UserInfoVo();
                    BeanUtils.copyProperties(user, userInfoVo);
                    moduleTopicVo.setUserInfoVo(userInfoVo);
                }
            });
            return moduleTopicVo;
        });
        return moduleTopicVoIPage;
    }
}




