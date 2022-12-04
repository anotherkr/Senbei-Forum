package com.yhz.senbeiforummain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.util.ImgUrlUtil;
import com.yhz.senbeiforummain.constant.SortType;
import com.yhz.senbeiforummain.domain.Module;
import com.yhz.senbeiforummain.domain.ModuleTopic;
import com.yhz.senbeiforummain.domain.ModuleTopicReply;
import com.yhz.senbeiforummain.domain.User;
import com.yhz.senbeiforummain.domain.dto.ModuleTopicPageDto;
import com.yhz.senbeiforummain.domain.dto.PublishTopicDto;
import com.yhz.senbeiforummain.domain.vo.ModuleTopicVo;
import com.yhz.senbeiforummain.domain.vo.TopicDetailVo;
import com.yhz.senbeiforummain.domain.vo.TopicReplyVo;
import com.yhz.senbeiforummain.domain.vo.UserInfoVo;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.mapper.ModuleMapper;
import com.yhz.senbeiforummain.mapper.ModuleTopicReplyMapper;
import com.yhz.senbeiforummain.mapper.UserMapper;
import com.yhz.senbeiforummain.service.IModuleTopicService;
import com.yhz.senbeiforummain.mapper.ModuleTopicMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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
    @Resource
    private ModuleTopicReplyMapper moduleTopicReplyMapper;
    @Resource
    private ModuleMapper moduleMapper;
    /**
     * 主贴分页查询
     * @param moduleTopicPageDto 查询条件
     * @return
     * @throws BusinessException
     */
    @Override
    public IPage<ModuleTopicVo> pageList(ModuleTopicPageDto moduleTopicPageDto) throws BusinessException {
        Integer pageSize = moduleTopicPageDto.getPageSize();
        Integer currentPage = moduleTopicPageDto.getCurrentPage();
        Long moduleId = moduleTopicPageDto.getModuleId();
        String title = moduleTopicPageDto.getTitle();
        Integer sortType = moduleTopicPageDto.getSortType();
        if (pageSize == null || currentPage == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        IPage<ModuleTopic> page = new Page<>(currentPage, pageSize);
        QueryWrapper<ModuleTopic> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(title), "title", title);
        wrapper.eq(moduleId!=null,"module_id", moduleId);
        //默认按热度排序
        if (sortType == null || sortType.equals(SortType.SORT_BY_HEAT)) {
            wrapper.orderByDesc("heat");
        } else if (sortType.equals(SortType.SORT_BY_TIME)) {
            wrapper.orderByDesc("create_time");
        }
        IPage<ModuleTopic> moduleTopicIPage = this.page(page, wrapper);
        //若查询到的数据为空，抛出异常
        if (moduleTopicIPage.getRecords().size() == 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        List<User> userList = userMapper.selectList(null);
        List<Module> moduleList = moduleMapper.selectList(null);
        //传入主贴对应的用户信息
        IPage<ModuleTopicVo> moduleTopicVoIPage = moduleTopicIPage.convert(moduleTopic -> {
            ModuleTopicVo moduleTopicVo = new ModuleTopicVo();
            BeanUtils.copyProperties(moduleTopic, moduleTopicVo);
            //解析图片地址字符串放入字符串数组
            String imgUrls = moduleTopic.getImgUrls();
            if (!StringUtils.isEmpty(imgUrls)) {
                moduleTopicVo.setImgUrlArray(ImgUrlUtil.imgUrlJsonToArray(imgUrls));
            }
            //获取用户信息
            Long userId = moduleTopic.getUserId();
            User user = userList.stream().filter(item -> item.getId().equals(userId)).findFirst().get();
            UserInfoVo userInfoVo = new UserInfoVo();
            BeanUtils.copyProperties(user, userInfoVo);
            moduleTopicVo.setUserInfoVo(userInfoVo);
            //获取模块名
            Module selectModule = moduleList.stream().filter(module -> {
               return module.getId().equals(moduleTopic.getModuleId());
            }).findFirst().get();
            moduleTopicVo.setModuleName(selectModule.getName());
            return moduleTopicVo;
        });

        return moduleTopicVoIPage;
    }

    @Override
    public void publish(PublishTopicDto publishTopicDto) throws BusinessException {

        ModuleTopic moduleTopic = new ModuleTopic();
        BeanUtils.copyProperties(publishTopicDto, moduleTopic);
        //处理图片地址数组转json
        String[] imgUrlArray = publishTopicDto.getImgUrlArray();
        if (ArrayUtils.isNotEmpty(imgUrlArray)) {
            String imgUrls = ImgUrlUtil.imgUrlArrayToJson(imgUrlArray);
            moduleTopic.setImgUrls(imgUrls);
        }
        boolean save = this.save(moduleTopic);
        if (!save) {
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
    }

    @Override
    public TopicDetailVo getTopicDetailVo(Long topicId) {
        ModuleTopic moduleTopic = this.getById(topicId);
        TopicDetailVo topicDetailVo = new TopicDetailVo();
        BeanUtils.copyProperties(moduleTopic, topicDetailVo);
        //处理图片
        String imgUrls = moduleTopic.getImgUrls();
        String[] imgUrlsToArray = ImgUrlUtil.imgUrlJsonToArray(imgUrls);
        topicDetailVo.setImgUrlArray(imgUrlsToArray);
        //获取用户信息
        User user = userMapper.selectById(moduleTopic.getUserId());
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);
        topicDetailVo.setUserInfoVo(userInfoVo);
        //获取主贴回复
        QueryWrapper<ModuleTopicReply> replyWrapper = new QueryWrapper<>();
        replyWrapper.eq("topic_id", topicId);
        List<TopicReplyVo> topicReplyVoList = moduleTopicReplyMapper.selectTopicReplyVoListByTopicId(topicId);
        topicDetailVo.setTopicReplyVoList(topicReplyVoList);

        return topicDetailVo;
    }

    @Override
    public List<ModuleTopicVo> sortlist(Integer sortType) {

        QueryWrapper<ModuleTopic> queryWrapper = new QueryWrapper<>();
        if (SortType.SORT_BY_HEAT.equals(sortType)) {
            queryWrapper.orderByDesc("heat");
        } else if (SortType.SORT_BY_TIME.equals(sortType)) {
            queryWrapper.orderByDesc("create_time");
        }
        List<ModuleTopic> moduleTopicList = this.list(queryWrapper);
        List<ModuleTopicVo> moduleTopicVoList = moduleTopicList.stream().map(item -> {
            ModuleTopicVo moduleTopicVo = new ModuleTopicVo();
            BeanUtils.copyProperties(item, moduleTopicVo);
            return moduleTopicVo;
        }).collect(Collectors.toList());

        return moduleTopicVoList;
    }
}




