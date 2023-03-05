package com.yhz.senbeiforummain.service;

import com.yhz.senbeiforummain.model.entity.ReplySecond;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhz.senbeiforummain.model.dto.replysecond.ReplySecondQueryRequst;

/**
* @author 吉良吉影
* @description 针对表【reply_second(二级回复表)】的数据库操作Service
* @createDate 2022-11-13 16:08:53
*/
public interface IReplySecondService extends IService<ReplySecond> {

    void reply(ReplySecondQueryRequst replySecondQueryRequst, Long userId);
}
