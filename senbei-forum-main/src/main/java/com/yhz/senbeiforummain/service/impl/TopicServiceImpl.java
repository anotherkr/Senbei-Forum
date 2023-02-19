package com.yhz.senbeiforummain.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.constant.PageConstant;
import com.yhz.commonutil.constant.SortConstant;
import com.yhz.commonutil.util.ImgUrlUtil;
import com.yhz.senbeiforummain.constant.SortType;
import com.yhz.senbeiforummain.mapper.TopicMapper;
import com.yhz.senbeiforummain.model.entity.Module;
import com.yhz.senbeiforummain.model.entity.Topic;
import com.yhz.senbeiforummain.model.entity.TopicReply;
import com.yhz.senbeiforummain.model.entity.User;
import com.yhz.senbeiforummain.model.dto.topic.TopicQueryRequest;
import com.yhz.senbeiforummain.model.dto.topic.TopicAddRequst;
import com.yhz.senbeiforummain.model.vo.TopicVo;
import com.yhz.senbeiforummain.model.vo.TopicDetailVo;
import com.yhz.senbeiforummain.model.vo.TopicReplyVo;
import com.yhz.senbeiforummain.model.vo.UserInfoVo;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.mapper.ModuleMapper;
import com.yhz.senbeiforummain.mapper.TopicReplyMapper;
import com.yhz.senbeiforummain.mapper.UserMapper;
import com.yhz.senbeiforummain.service.ITopicService;
import com.yhz.senbeiforummain.util.PageUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 吉良吉影
 * @description 针对表【topic(主贴表)】的数据库操作Service实现
 * @createDate 2022-11-13 16:08:53
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic>
        implements ITopicService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private TopicReplyMapper topicReplyMapper;
    @Resource
    private ModuleMapper moduleMapper;
    /**
     * 主贴分页查询
     * @param topicQueryRequest 查询条件
     * @return
     * @throws BusinessException
     */
    @Override
    public IPage<TopicVo> pageList(TopicQueryRequest topicQueryRequest) throws BusinessException {
        Optional.ofNullable(topicQueryRequest).orElseThrow(() -> new BusinessException(ErrorCode.PARAMS_ERROR));
        IPage<Topic> page = PageUtil.vaildPageParam(topicQueryRequest.getCurrent(), topicQueryRequest.getPageSize());
        Long id = topicQueryRequest.getId();
        Long moduleId = topicQueryRequest.getModuleId();
        String title = topicQueryRequest.getTitle();
        String content = topicQueryRequest.getContent();
        Long userId = topicQueryRequest.getUserId();
        String sortField = topicQueryRequest.getSortField();
        String sortOrder = topicQueryRequest.getSortOrder();
        QueryWrapper<Topic> wrapper = new QueryWrapper<>();
        wrapper.like(!StrUtil.isBlank(title), "title", title);
        wrapper.like(!StrUtil.isBlank(content), "content", title);
        wrapper.eq(id!=null,"id", id);
        wrapper.eq(moduleId!=null,"module_id", moduleId);
        wrapper.eq(userId!=null,"user_id", userId);
        if (!StrUtil.isBlank(sortField)) {
            if (sortOrder.equals(SortConstant.SORT_ORDER_DESC)) {
                wrapper.orderByDesc(sortField);
            } else if(sortOrder.equals(SortConstant.SORT_ORDER_ASC)){
                wrapper.orderByAsc(sortField);
            }else {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        IPage<Topic> moduleTopicIPage = this.page(page, wrapper);
        //若查询到的数据为空，抛出异常
        if (moduleTopicIPage.getRecords().size() == 0) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        List<User> userList = userMapper.selectList(null);
        List<Module> moduleList = moduleMapper.selectList(null);
        //传入主贴对应的用户信息
        IPage<TopicVo> moduleTopicVoIPage = moduleTopicIPage.convert(moduleTopic -> {
            TopicVo topicVo = new TopicVo();
            BeanUtils.copyProperties(moduleTopic, topicVo);
            //解析图片地址字符串放入字符串数组
            String imgUrls = moduleTopic.getImgUrls();
            if (!StrUtil.isBlank(imgUrls)) {
                topicVo.setImgUrlArray(ImgUrlUtil.imgUrlJsonToArray(imgUrls));
            }
            //获取用户信息
            Long topicUserId = moduleTopic.getUserId();
            User user = userList.stream().filter(item -> item.getId().equals(topicUserId)).findFirst().get();
            UserInfoVo userInfoVo = new UserInfoVo();
            BeanUtils.copyProperties(user, userInfoVo);
            topicVo.setUserInfoVo(userInfoVo);
            //获取模块名和背景图片
            Module selectModule = moduleList.stream().filter(module -> module.getId().equals(moduleTopic.getModuleId())).findFirst().get();
            topicVo.setModuleName(selectModule.getName());
            topicVo.setModuleBackgroundImgUrl(selectModule.getBackgroundImgUrl());
            return topicVo;
        });

        return moduleTopicVoIPage;
    }

    @Override
    public void publish(TopicAddRequst topicAddRequst) throws BusinessException {

        Topic topic = new Topic();
        BeanUtils.copyProperties(topicAddRequst, topic);
        //处理图片地址数组转json
        String[] imgUrlArray = topicAddRequst.getImgUrlArray();
        if (ArrayUtils.isNotEmpty(imgUrlArray)) {
            String imgUrls = ImgUrlUtil.imgUrlArrayToJson(imgUrlArray);
            topic.setImgUrls(imgUrls);
        }
        boolean save = this.save(topic);
        if (!save) {
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
    }

    @Override
    public TopicDetailVo getTopicDetailVo(Long topicId) {
        Topic topic = this.getById(topicId);
        TopicDetailVo topicDetailVo = new TopicDetailVo();
        BeanUtils.copyProperties(topic, topicDetailVo);
        //处理图片
        String imgUrls = topic.getImgUrls();
        String[] imgUrlsToArray = ImgUrlUtil.imgUrlJsonToArray(imgUrls);
        topicDetailVo.setImgUrlArray(imgUrlsToArray);
        //获取用户信息
        User user = userMapper.selectById(topic.getUserId());
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);
        topicDetailVo.setUserInfoVo(userInfoVo);
        //获取主贴回复
        QueryWrapper<TopicReply> replyWrapper = new QueryWrapper<>();
        replyWrapper.eq("topic_id", topicId);
        List<TopicReplyVo> topicReplyVoList = topicReplyMapper.selectTopicReplyVoListByTopicId(topicId);
        topicDetailVo.setTopicReplyVoList(topicReplyVoList);

        return topicDetailVo;
    }

    @Override
    public List<TopicVo> sortlist(Integer sortType) {

        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        if (SortType.SORT_BY_HEAT.equals(sortType)) {
            queryWrapper.orderByDesc("heat");
        } else if (SortType.SORT_BY_TIME.equals(sortType)) {
            queryWrapper.orderByDesc("create_time");
        }
        List<Topic> topicList = this.list(queryWrapper);
        List<TopicVo> topicVoList = topicList.stream().map(item -> {
            TopicVo topicVo = new TopicVo();
            BeanUtils.copyProperties(item, topicVo);
            return topicVo;
        }).collect(Collectors.toList());

        return topicVoList;
    }
}




