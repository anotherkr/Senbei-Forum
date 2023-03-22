package com.yhz.senbeiforummain.aop;

import com.yhz.senbeiforummain.constant.rediskey.RedisTopicKey;
import com.yhz.senbeiforummain.model.dto.topic.TopicDetailQueryRequest;
import com.yhz.senbeiforummain.util.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yanhuanzhan
 * @date 2023/3/20 - 15:41
 */
@Component
@Slf4j
@Aspect
public class ClickNumAspect {
    @Resource
    private RedisCache redisCache;
    /**
     * 拿到@ClickNum注解注释的方法，这里就是切点
     */
    @Pointcut("@annotation(com.yhz.senbeiforummain.common.annotation.ClickNum)")
    private void clickNum(){

    }
    /**
     * 调用方法后都会进行统计操作，写入redis
      */
    @After("clickNum()")
    public void afterMethod(JoinPoint joinpoint) {
        Object[] args = joinpoint.getArgs();
        TopicDetailQueryRequest topicDetailQueryRequest = (TopicDetailQueryRequest) args[0];
        Long topicId = topicDetailQueryRequest.getTopicId();

        Map<String, Integer> cacheMap = redisCache.getCacheMap(RedisTopicKey.getClickNum, "");
        if (cacheMap == null) {
            cacheMap = new HashMap<>();
            cacheMap.put(topicId.toString(), 1);
        }else {
            cacheMap.put(topicId.toString(), cacheMap.getOrDefault(topicId.toString(), 0) + 1);
        }
        redisCache.setCacheMap(RedisTopicKey.getClickNum,"",cacheMap);
    }
}
