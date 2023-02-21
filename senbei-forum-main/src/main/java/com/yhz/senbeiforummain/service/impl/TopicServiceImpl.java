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
import com.yhz.senbeiforummain.model.to.TopicTo;
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
import org.springframework.transaction.annotation.Transactional;
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
    @Resource
    private TopicMapper topicMapper;
    /**
     * 主贴分页查询
     * @param topicQueryRequest 查询条件
     * @return
     * @throws BusinessException
     */
    @Override
    public IPage<TopicVo> pageList(TopicQueryRequest topicQueryRequest) throws BusinessException {
        IPage<Topic> page = PageUtil.vaildPageParam(topicQueryRequest.getCurrent(), topicQueryRequest.getPageSize());
        String sortField = topicQueryRequest.getSortField();
        String sortOrder = topicQueryRequest.getSortOrder();
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
        topicQueryRequest.setSortField(sortField);
        topicQueryRequest.setSortOrder(sortOrder);
        IPage<TopicTo> topicToIPage=topicMapper.selectTopicToPage(page, topicQueryRequest);
        IPage<TopicVo> topicVoIPage = topicToIPage.convert(item -> {
            TopicVo topicVo = new TopicVo();
            BeanUtils.copyProperties(item, topicVo);
            topicVo.setImgUrlArray(ImgUrlUtil.imgUrlJsonToArray(item.getImgUrls()));
            return topicVo;
        });

        return topicVoIPage;
    }

    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void publish(TopicAddRequst topicAddRequst) throws BusinessException {
        Long moduleId = topicAddRequst.getModuleId();
        Long userId = topicAddRequst.getUserId();
        if (userId == null || moduleId == null || userId <= 0 || moduleId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
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
        Module module = moduleMapper.selectById(moduleId);
        long concernNum = module.getConcernNum() + 1;
        module.setConcernNum(concernNum);
        int update = moduleMapper.updateById(module);
        if (update <= 0) {
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
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




