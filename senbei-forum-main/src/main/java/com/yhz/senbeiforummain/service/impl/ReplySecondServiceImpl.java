package com.yhz.senbeiforummain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.senbeiforummain.domain.ReplySecond;
import com.yhz.senbeiforummain.domain.dto.ReplySecondDto;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.service.IReplySecondService;
import com.yhz.senbeiforummain.mapper.ReplySecondMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
* @author 吉良吉影
* @description 针对表【reply_second(二级回复表)】的数据库操作Service实现
* @createDate 2022-11-13 16:08:53
*/
@Service
public class ReplySecondServiceImpl extends ServiceImpl<ReplySecondMapper, ReplySecond>
    implements IReplySecondService {

    @Override
    public void reply(ReplySecondDto replySecondDto) {
        ReplySecond replySecond = new ReplySecond();
        BeanUtils.copyProperties(replySecondDto, replySecond);
        boolean save = this.save(replySecond);
        if (!save) {
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
    }
}




