package com.yhz.senbeiforummain.service;

import com.yhz.senbeiforummain.domain.ModuleTopicReply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhz.senbeiforummain.domain.dto.ModuleTopicReplyDto;

/**
* @author 吉良吉影
* @description 针对表【module_topic_reply(主贴回复表)】的数据库操作Service
* @createDate 2022-11-13 16:08:53
*/
public interface IModuleTopicReplyService extends IService<ModuleTopicReply> {

    void reply(ModuleTopicReplyDto moduleTopicReplyDto);
}
