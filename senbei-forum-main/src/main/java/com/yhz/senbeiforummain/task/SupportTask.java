package com.yhz.senbeiforummain.task;

import com.yhz.senbeiforummain.constant.rediskey.RedisTopicKey;
import com.yhz.senbeiforummain.constant.rediskey.RedisTopicReplyKey;
import com.yhz.senbeiforummain.model.entity.Topic;
import com.yhz.senbeiforummain.model.entity.TopicReply;
import com.yhz.senbeiforummain.model.entity.TopicReplySupport;
import com.yhz.senbeiforummain.model.entity.TopicSupport;
import com.yhz.senbeiforummain.service.ITopicReplyService;
import com.yhz.senbeiforummain.service.ITopicService;
import com.yhz.senbeiforummain.service.ITopicReplySupportService;
import com.yhz.senbeiforummain.service.ITopicSupportService;
import com.yhz.senbeiforummain.util.RedisCache;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yanhuanzhan
 * @date 2023/3/9 - 15:29
 */
@Component
public class SupportTask {
    @Resource
    private RedisCache redisCache;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private ITopicReplySupportService topicReplySupportService;
    @Resource
    private ITopicReplyService topicReplyService;
    @Resource
    private ITopicService topicService;
    @Resource
    private ITopicSupportService topicSupportService;
    /**
     * 秒 分 时 日 月 周
     * 以秒为例
     * *：每秒都执行
     * 1-3：从第1秒开始执行，到第3秒结束执行
     * 0/3：从第0秒开始，每隔3秒执行1次
     * 1,2,3：在指定的第1、2、3秒执行
     * ?：不指定
     * 日和周不能同时制定，指定其中之一，则另一个设置为?
     */


    /**
     * 记录一级回复点赞信息(每天0点触发)
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void topicReplySupportInfoTask() {
        Cursor<Map.Entry<String, Integer>> cursor = redisTemplate.opsForHash().scan(RedisTopicReplyKey.getSupportInfo.getPrefix(), ScanOptions.NONE);
        List<TopicReplySupport> supportList = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<String,Integer> next = cursor.next();
            String key = next.getKey();
            Integer value = next.getValue();
            String[] split = key.split("::");
            TopicReplySupport support = new TopicReplySupport();
            support.setIsSupport(value);
            support.setTopicReplyId(Long.valueOf(split[0]));
            support.setUserId(Long.valueOf(split[1]));
            supportList.add(support);
            redisCache.delCacheMapValue(RedisTopicReplyKey.getSupportInfo, "",key);
        }
        topicReplySupportService.saveBatch(supportList);
    }

    /**
     * 记录一级回复点赞次数(每小时执行一次)
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void topicReplySupportCountTask() {
        Cursor<Map.Entry<String, Integer>> cursor = redisTemplate.opsForHash().scan(RedisTopicReplyKey.getSupportInfo.getPrefix(), ScanOptions.NONE);
        List<TopicReply> replyArrayList = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<String,Integer> next = cursor.next();
            String key = next.getKey();
            Integer value = next.getValue();
            TopicReply topicReply = topicReplyService.getById(key);
            if(topicReply!=null){
                Integer supportNum = topicReply.getSupportNum()+value;
                topicReply.setSupportNum(supportNum);
                replyArrayList.add(topicReply);
            }
            redisCache.delCacheMapValue(RedisTopicReplyKey.getSupportCount, "",key);
        }
        topicReplyService.updateBatchById(replyArrayList);
    }
    /**
     * 记录帖子点赞信息(每天0点触发)
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void topicSupportInfoTask() {
        Cursor<Map.Entry<String, Integer>> cursor = redisTemplate.opsForHash().scan(RedisTopicKey.getSupportInfo.getPrefix(), ScanOptions.NONE);
        List<TopicSupport> supportList = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<String,Integer> next = cursor.next();
            String key = next.getKey();
            Integer value = next.getValue();
            String[] split = key.split("::");
            TopicSupport support = new TopicSupport();
            support.setIsSupport(value);
            support.setTopicId(Long.valueOf(split[0]));
            support.setUserId(Long.valueOf(split[1]));
            supportList.add(support);
            redisCache.delCacheMapValue(RedisTopicKey.getSupportInfo, "",key);
        }
        topicSupportService.saveBatch(supportList);
    }

    /**
     * 记录帖子点赞次数(每小时执行一次)
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void topicSupportCountTask() {
        Cursor<Map.Entry<String, Integer>> cursor = redisTemplate.opsForHash().scan(RedisTopicReplyKey.getSupportInfo.getPrefix(), ScanOptions.NONE);
        List<Topic> topicList = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<String,Integer> next = cursor.next();
            String key = next.getKey();
            Integer value = next.getValue();
            Topic topic = topicService.getById(key);
            Integer supportNum = topic.getSupportNum()+value;
            topic.setSupportNum(supportNum);
            topicList.add(topic);
            redisCache.delCacheMapValue(RedisTopicKey.getSupportCount, "",key);
        }
        topicService.updateBatchById(topicList);
    }
}
