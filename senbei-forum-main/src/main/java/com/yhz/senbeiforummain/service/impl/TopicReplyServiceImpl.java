package com.yhz.senbeiforummain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.util.ImgUrlUtil;
import com.yhz.senbeiforummain.model.entity.TopicReply;
import com.yhz.senbeiforummain.model.dto.topicreply.TopicReplyRequst;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.service.ITopicReplyService;
import com.yhz.senbeiforummain.mapper.TopicReplyMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
* @author 吉良吉影
* @description 针对表【topic_reply(主贴回复表)】的数据库操作Service实现
* @createDate 2022-11-13 16:08:53
*/
@Service
public class TopicReplyServiceImpl extends ServiceImpl<TopicReplyMapper, TopicReply>
    implements ITopicReplyService {

    @Override
    public void reply(TopicReplyRequst topicReplyRequst, Long userId) {
        TopicReply topicReply = new TopicReply();
        topicReply.setUserId(userId);
        BeanUtils.copyProperties(topicReplyRequst, topicReply);
        //处理图片
        String[] imgUrlArray = topicReplyRequst.getImgUrlArray();
        if (ArrayUtils.isNotEmpty(imgUrlArray)) {
            String imgUrls = ImgUrlUtil.imgUrlArrayToJson(imgUrlArray);
            topicReply.setImgUrls(imgUrls);
        }

        boolean save = this.save(topicReply);
        if (!save) {
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
    }
}




