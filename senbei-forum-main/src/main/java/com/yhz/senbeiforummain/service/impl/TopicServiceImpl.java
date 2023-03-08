package com.yhz.senbeiforummain.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.constant.SortConstant;
import com.yhz.commonutil.util.ImgUrlUtil;
import com.yhz.senbeiforummain.mapper.TopicMapper;
import com.yhz.senbeiforummain.model.dto.topic.TopicDetailQueryRequest;
import com.yhz.senbeiforummain.model.entity.Module;
import com.yhz.senbeiforummain.model.entity.Topic;
import com.yhz.senbeiforummain.model.entity.User;
import com.yhz.senbeiforummain.model.dto.topic.TopicQueryRequest;
import com.yhz.senbeiforummain.model.dto.topic.TopicAddRequst;
import com.yhz.senbeiforummain.model.to.TopicReplyTo;
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
import com.yhz.senbeiforummain.util.IpUtils;
import com.yhz.senbeiforummain.util.PageUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author 吉良吉影
 * @description 针对表【topic(主贴表)】的数据库操作Service实现
 * @createDate 2022-11-13 16:08:53
 */
@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic>
        implements ITopicService {
    @Value("${jwt.tokenHeader}")
    private String header;
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
    public void publish(TopicAddRequst topicAddRequst, Long userId, HttpServletRequest request) throws BusinessException {
        Long moduleId = topicAddRequst.getModuleId();
        //获取ip所属地
        String city;
        try {
            city = IpUtils.getCity(request);
        } catch (Exception e) {
            log.error("获取IP所属地失败:{}",e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        if (userId == null || moduleId == null || userId <= 0 || moduleId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Topic topic = new Topic();
        topic.setUserId(userId);
        topic.setCity(city);
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
        Optional.ofNullable(module).orElseThrow(() -> new BusinessException(ErrorCode.PARAMS_ERROR));
        long concernNum = module.getConcernNum() + 1;
        module.setConcernNum(concernNum);
        int update = moduleMapper.updateById(module);
        if (update <= 0) {
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public TopicDetailVo getTopicDetailVo(TopicDetailQueryRequest topicDetailQueryRequest) {
        Long topicId = topicDetailQueryRequest.getTopicId();
        long current = topicDetailQueryRequest.getCurrent();
        long pageSize = topicDetailQueryRequest.getPageSize();
        String sortField = topicDetailQueryRequest.getSortField();
        String sortOrder = topicDetailQueryRequest.getSortOrder();
        Long userId = topicDetailQueryRequest.getUserId();

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
        IPage<TopicReplyTo> topicReplyToIPage = PageUtil.vaildPageParam(current, pageSize);
        String checkSortField = PageUtil.sqlInject(sortField);
        IPage<TopicReplyTo> finalTopicReplyToIPage = topicReplyMapper.selectTopicReplyVoIPageByTopicId(topicReplyToIPage, topicId, userId, checkSortField, sortOrder);
        IPage<TopicReplyVo> topicReplyVoIPage = finalTopicReplyToIPage.convert(item -> {
            String urls = item.getImgUrls();
            String[] toArray = ImgUrlUtil.imgUrlJsonToArray(urls);
            TopicReplyVo topicReplyVo = new TopicReplyVo();
            topicReplyVo.setImgUrlArray(toArray);
            BeanUtils.copyProperties(item, topicReplyVo);
            return topicReplyVo;
        });
        topicDetailVo.setTopicReplyVoIPage(topicReplyVoIPage);
        return topicDetailVo;
    }


}




