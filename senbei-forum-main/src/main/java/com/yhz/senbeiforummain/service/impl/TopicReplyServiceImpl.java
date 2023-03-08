package com.yhz.senbeiforummain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.util.ImgUrlUtil;
import com.yhz.senbeiforummain.constant.rediskey.RedisTopicReplyKey;
import com.yhz.senbeiforummain.model.dto.topicreplysupport.TopicReplySupportAddRequest;
import com.yhz.senbeiforummain.model.entity.TopicReply;
import com.yhz.senbeiforummain.model.dto.topicreply.TopicReplyAddRequst;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.model.enums.SupportEnum;
import com.yhz.senbeiforummain.service.ITopicReplyService;
import com.yhz.senbeiforummain.mapper.TopicReplyMapper;
import com.yhz.senbeiforummain.util.IpUtils;
import com.yhz.senbeiforummain.util.RedisCache;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
* @author 吉良吉影
* @description 针对表【topic_reply(主贴回复表)】的数据库操作Service实现
* @createDate 2022-11-13 16:08:53
*/
@Service
public class TopicReplyServiceImpl extends ServiceImpl<TopicReplyMapper, TopicReply>
    implements ITopicReplyService {
    @Resource
    private RedisCache redisCache;
    @Override
    public void reply(TopicReplyAddRequst topicReplyAddRequst, Long userId, HttpServletRequest request) {
        String city;
        try {
             city = IpUtils.getCity(request);
        } catch (Exception e) {
            log.error("获取IP所属地失败:{}",e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        TopicReply topicReply = new TopicReply();
        topicReply.setCity(city);
        topicReply.setUserId(userId);
        BeanUtils.copyProperties(topicReplyAddRequst, topicReply);
        //处理图片
        String[] imgUrlArray = topicReplyAddRequst.getImgUrlArray();
        if (ArrayUtils.isNotEmpty(imgUrlArray)) {
            String imgUrls = ImgUrlUtil.imgUrlArrayToJson(imgUrlArray);
            topicReply.setImgUrls(imgUrls);
        }

        boolean save = this.save(topicReply);
        if (!save) {
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
    }

    @Override
    public void support(Long topicReplyId, Long userId) {
        String key = topicReplyId.toString().concat("::").concat(userId.toString());
        Map<String,Object> supportInfo = redisCache.getCacheMap(RedisTopicReplyKey.getSupportInfo,null);
        TopicReplySupportAddRequest topicReplySupportAddRequest = (TopicReplySupportAddRequest) supportInfo.get(key);
        if (topicReplySupportAddRequest!=null&&topicReplySupportAddRequest.getIsSupport().equals(SupportEnum.NO_SUPPORT)) {

        }
    }
}




