package com.yhz.senbeiforummain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.util.ImgUrlUtil;
import com.yhz.senbeiforummain.domain.ModuleTopicReply;
import com.yhz.senbeiforummain.domain.dto.ModuleTopicReplyDto;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.service.IModuleTopicReplyService;
import com.yhz.senbeiforummain.mapper.ModuleTopicReplyMapper;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
* @author 吉良吉影
* @description 针对表【module_topic_reply(主贴回复表)】的数据库操作Service实现
* @createDate 2022-11-13 16:08:53
*/
@Service
public class ModuleTopicReplyServiceImpl extends ServiceImpl<ModuleTopicReplyMapper, ModuleTopicReply>
    implements IModuleTopicReplyService {

    @Override
    public void reply(ModuleTopicReplyDto moduleTopicReplyDto) {
        ModuleTopicReply moduleTopicReply = new ModuleTopicReply();
        BeanUtils.copyProperties(moduleTopicReplyDto, moduleTopicReply);
        //处理图片
        String[] imgUrlArray = moduleTopicReplyDto.getImgUrlArray();
        if (ArrayUtils.isNotEmpty(imgUrlArray)) {
            String imgUrls = ImgUrlUtil.imgUrlArrayToJson(imgUrlArray);
            moduleTopicReply.setImgUrls(imgUrls);
        }

        boolean save = this.save(moduleTopicReply);
        if (!save) {
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
    }
}




