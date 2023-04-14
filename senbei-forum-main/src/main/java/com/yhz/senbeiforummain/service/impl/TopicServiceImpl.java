package com.yhz.senbeiforummain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.common.PageRequest;
import com.yhz.commonutil.util.ImgUrlUtil;
import com.yhz.senbeiforummain.constant.rediskey.RedisTopicKey;
import com.yhz.senbeiforummain.constant.rediskey.RedisTopicReplyKey;
import com.yhz.senbeiforummain.mapper.TopicMapper;
import com.yhz.senbeiforummain.model.dto.topic.TopicDetailQueryRequest;
import com.yhz.senbeiforummain.model.entity.Module;
import com.yhz.senbeiforummain.model.entity.Topic;
import com.yhz.senbeiforummain.model.entity.User;
import com.yhz.senbeiforummain.model.dto.topic.TopicQueryRequest;
import com.yhz.senbeiforummain.model.dto.topic.TopicAddRequst;
import com.yhz.senbeiforummain.model.enums.SupportEnum;
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
import com.yhz.senbeiforummain.util.RedisCache;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedisCache redisCache;

    /**
     * 主贴分页查询
     *
     * @param topicQueryRequest 查询条件
     * @param userId
     * @return
     * @throws BusinessException
     */
    @Override
    public IPage<TopicVo> pageList(TopicQueryRequest topicQueryRequest, Long userId) throws BusinessException {
        IPage<Topic> page = PageUtil.vaildPageParam(topicQueryRequest.getCurrent(), topicQueryRequest.getPageSize());
        String sortField = topicQueryRequest.getSortField();
        Long moduleId = topicQueryRequest.getModuleId();
        //校验排序参数，防止sql注入，默认按时间降序排序
        sortField = PageUtil.sqlInject(sortField);
        topicQueryRequest.setSortField(sortField);
        IPage<TopicTo> topicToIPage = topicMapper.selectTopicToPage(page, topicQueryRequest);
        IPage<TopicVo> topicVoIPage = topicToIPage.convert(item -> {
            TopicVo topicVo = new TopicVo();
            BeanUtils.copyProperties(item, topicVo);
            topicVo.setImgUrlArray(ImgUrlUtil.imgUrlJsonToArray(item.getImgUrls()));
            return topicVo;
        });
        //添加模块信息
        List<Long> moduleIdList = topicVoIPage.getRecords().stream().map(item -> item.getModuleId()).collect(Collectors.toList());
        Map<Long, Module> moduleMap = getModuleByModuleIdList(moduleIdList);
        topicVoIPage.getRecords().forEach(topicVo -> {
            Module module = moduleMap.get(topicVo.getModuleId());
            if (module != null) {
                topicVo.setModuleName(module.getName());
                topicVo.setModuleId(module.getId());
                //设置是否点赞
                topicVo.setIsSupport(judgeIsSupport(topicVo.getId(), userId));
            }
            //加上缓存中的点赞数
            topicVo.setSupportNum(topicVo.getSupportNum() + getRedisTopicSupportNum(topicVo.getId()));
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
            log.error("获取IP所属地失败:{}", e);
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
        long topicNum = module.getTopicNum() + 1;
        module.setTopicNum(topicNum);
        int update = moduleMapper.updateById(module);
        if (update <= 0) {
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
    }

    @Override
    public TopicDetailVo getTopicDetailVo(TopicDetailQueryRequest topicDetailQueryRequest, Long currentUserId) {
        Long topicId = topicDetailQueryRequest.getTopicId();
        long current = topicDetailQueryRequest.getCurrent();
        long pageSize = topicDetailQueryRequest.getPageSize();
        String sortField = topicDetailQueryRequest.getSortField();
        String sortOrder = topicDetailQueryRequest.getSortOrder();
        Long userId = topicDetailQueryRequest.getUserId();
        sortField = PageUtil.sqlInject(sortField);
        Topic topic = this.getById(topicId);
        if (topic == null) {
            throw new BusinessException("帖子不存在",50000,"");
        }
        TopicDetailVo topicDetailVo = new TopicDetailVo();
        topicDetailVo.setTopicId(topicId);
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
            if (currentUserId != null) {
                //设置是否点赞
                String hKey = item.getId().toString().concat("::").concat(currentUserId.toString());
                Map<String, Integer> cacheMap = redisCache.getCacheMap(RedisTopicReplyKey.getSupportInfo, "");
                if (cacheMap != null) {
                    Integer isSupport = cacheMap.getOrDefault(hKey,SupportEnum.NO_SUPPORT.getCode());
                    topicReplyVo.setIsSupport(isSupport);
                }
            }
            //设置点赞数
            topicReplyVo.setSupportNum(topicReplyVo.getSupportNum() + getRedisTopicReplySupportNum(topicReplyVo.getId()));
            return topicReplyVo;
        });
        topicDetailVo.setTopicReplyVoIPage(topicReplyVoIPage);
        //设置帖子是否点赞
        topicDetailVo.setIsSupport(judgeIsSupport(topicId, currentUserId));
        //加上缓存中的点赞数
        topicDetailVo.setSupportNum(topicDetailVo.getSupportNum() + getRedisTopicSupportNum(topicId));
        return topicDetailVo;
    }

    @Override
    public Integer support(Long topicId, Long userId) {
        DefaultRedisScript<Integer> redisScript = new DefaultRedisScript();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/support.lua")));
        redisScript.setResultType(Integer.class);
        String[] keys = {RedisTopicKey.getSupportInfo.getPrefix(),
                RedisTopicKey.getSupportCount.getPrefix()};
        Integer res = (Integer) redisTemplate.execute(redisScript, Arrays.asList(keys), topicId.toString(), userId.toString());
        return res;
    }

    @Override
    public IPage<TopicVo> userTopicPage(PageRequest pageRequest, Long userId) {
        Long current = pageRequest.getCurrent();
        Long pageSize = pageRequest.getPageSize();
        String sortField = pageRequest.getSortField();
        String sortOrder = pageRequest.getSortOrder();
        IPage<Topic> iPage = new Page<>(current, pageSize);
        QueryWrapper<Topic> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        PageUtil.dealSortWrapper(wrapper, sortField, sortOrder);
        IPage<Topic> topicIPage = baseMapper.selectPage(iPage, wrapper);
        IPage<TopicVo> topicVoIPage = topicIPage.convert(topic -> {
            TopicVo topicVo = new TopicVo();
            topicVo.setImgUrlArray(ImgUrlUtil.imgUrlJsonToArray(topic.getImgUrls()));
            BeanUtils.copyProperties(topic, topicVo);
            return topicVo;
        });
        //获取模块信息
        List<Long> moduleIdList = topicVoIPage.getRecords().stream().map(topicVo ->
                topicVo.getModuleId()
        ).collect(Collectors.toList());
        Map<Long, Module> moduleMap = getModuleByModuleIdList(moduleIdList);
        topicVoIPage.getRecords().forEach(topicVo -> {
            String moduleName = Optional.ofNullable(moduleMap.get(topicVo.getModuleId())).map(Module::getName).orElse("未知");
            topicVo.setModuleName(moduleName);
            //設置是否點贊
            topicVo.setIsSupport(judgeIsSupport(topicVo.getId(), userId));
            //加上缓存中的点赞数
            topicVo.setSupportNum(topicVo.getSupportNum() + getRedisTopicSupportNum(topicVo.getId()));
        });
        //设置用户信息
        User user = userMapper.selectById(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);
        topicVoIPage.getRecords().forEach(topicVo -> {
            topicVo.setUserInfoVo(userInfoVo);
        });
        return topicVoIPage;
    }


    /**
     * 判断是否点赞
     *
     * @param
     * @param userId
     * @return
     */
    private int judgeIsSupport(Long topicId, Long userId) {
        if (userId != null) {
            String hKey = topicId.toString().concat("::").concat(userId.toString());
            Map<String, Integer> cacheMap = redisCache.getCacheMap(RedisTopicKey.getSupportInfo, "");
            if (cacheMap != null && !cacheMap.isEmpty()) {
                Integer isSupport = cacheMap.getOrDefault(hKey,SupportEnum.NO_SUPPORT.getCode());
                return isSupport != null ? isSupport : SupportEnum.NO_SUPPORT.getCode();
            }
        }
        return SupportEnum.NO_SUPPORT.getCode();
    }

    private int getRedisTopicSupportNum(Long topicId) {
        if (topicId == null) {
            return 0;
        }
        Map<String, Integer> cacheMap = redisCache.getCacheMap(RedisTopicKey.getSupportCount, "");
        if (cacheMap != null && !cacheMap.isEmpty()) {
            if (cacheMap.containsKey(topicId.toString())) {
                return cacheMap.getOrDefault(topicId.toString(),SupportEnum.NO_SUPPORT.getCode());
            }
        }
        return 0;
    }

    private int getRedisTopicReplySupportNum(Long topicReplyId) {
        if (topicReplyId == null) {
            return 0;
        }
        Map<String, Integer> cacheMap = redisCache.getCacheMap(RedisTopicReplyKey.getSupportCount, "");
        if (cacheMap != null && !cacheMap.isEmpty()) {
            if (cacheMap.containsKey(topicReplyId.toString())) {
                return cacheMap.getOrDefault(topicReplyId.toString(),SupportEnum.NO_SUPPORT.getCode());
            }
        }
        return 0;
    }

    private Map<Long, Module> getModuleByModuleIdList(List<Long> moduleIdList) {
        HashMap<Long, Module> moduleMap = new HashMap<>();
        if (moduleIdList.size() > 0) {
            List<Module> moduleList = moduleMapper.selectBatchIds(moduleIdList);
            moduleList.forEach(module -> {
                Long id = module.getId();
                if (module!=null) {
                    moduleMap.put(id, module);
                }
            });
        }
        return moduleMap;
    }
}




