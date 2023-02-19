package com.yhz.senbeiforummain.mapper;

import com.yhz.senbeiforummain.model.entity.ReplySecond;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhz.senbeiforummain.model.vo.ReplySecondVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 吉良吉影
* @description 针对表【reply_second(二级回复表)】的数据库操作Mapper
* @createDate 2022-11-13 16:08:53
* @Entity com.yhz.senbeiforummain.domain.ReplySecond
*/
public interface ReplySecondMapper extends BaseMapper<ReplySecond> {
    /**
     * 根据主贴回复表id查询二级回复vo
     * @param topicReplyId
     * @return
     */
    List<ReplySecondVo> getReplySecondVoListByTopicReplyId(@Param("topicReplyId")Long topicReplyId);
}




