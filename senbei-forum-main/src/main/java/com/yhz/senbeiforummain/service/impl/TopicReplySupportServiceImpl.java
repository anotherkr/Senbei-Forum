package com.yhz.senbeiforummain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.senbeiforummain.model.entity.TopicReplySupport;
import com.yhz.senbeiforummain.service.ITopicReplySupportService;
import com.yhz.senbeiforummain.mapper.TopicReplySupportMapper;
import org.springframework.stereotype.Service;

/**
* @author 吉良吉影
* @description 针对表【topic_reply_support(一级回复点赞表)】的数据库操作Service实现
* @createDate 2023-03-07 18:13:39
*/
@Service
public class TopicReplySupportServiceImpl extends ServiceImpl<TopicReplySupportMapper, TopicReplySupport>
    implements ITopicReplySupportService {

}




