package com.yhz.senbeiforummain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.senbeiforummain.domain.ModuleTopic;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhz.senbeiforummain.domain.dto.ModuleTopicPageDto;
import com.yhz.senbeiforummain.domain.dto.PublishTopicDto;
import com.yhz.senbeiforummain.domain.vo.ModuleTopicVo;
import com.yhz.senbeiforummain.domain.vo.TopicDetailVo;
import com.yhz.senbeiforummain.exception.BusinessException;

import java.util.List;

/**
* @author 吉良吉影
* @description 针对表【module_topic(主贴表)】的数据库操作Service
* @createDate 2022-11-13 16:08:53
*/
public interface IModuleTopicService extends IService<ModuleTopic> {
    /**
     * 分页查询
     * @param moduleTopicPageDto 查询条件
     * @return
     */
    IPage<ModuleTopicVo> pageList(ModuleTopicPageDto moduleTopicPageDto) throws BusinessException;

    /**
     * 发布主贴
     * @param publishTopicDto
     */
    void publish(PublishTopicDto publishTopicDto);

    /**
     * 获取主贴详细信息
     * @param topicId
     * @return
     */
    TopicDetailVo getTopicDetailVo(Long topicId);

    /**
     * 查询所有并排序
     * @return
     */
    List<ModuleTopicVo> sortlist(Integer sortType);
}
