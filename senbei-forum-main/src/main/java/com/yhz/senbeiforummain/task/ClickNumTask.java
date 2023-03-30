package com.yhz.senbeiforummain.task;

import com.yhz.senbeiforummain.constant.rediskey.RedisTopicKey;
import com.yhz.senbeiforummain.constant.rediskey.RedisTopicReplyKey;
import com.yhz.senbeiforummain.model.entity.Topic;
import com.yhz.senbeiforummain.model.entity.TopicReply;
import com.yhz.senbeiforummain.service.ITopicService;
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
 * @date 2023/3/20 - 17:17
 */
@Component
public class ClickNumTask {
    @Resource
    private RedisCache redisCache;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private ITopicService topicService;
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
     * 记录帖子访问数(每15分钟执行一次)
     */
    @Scheduled(cron = "0 0/15 * * * ?")
    public void clickNumTask() {
        Cursor<Map.Entry<String, Integer>> cursor = redisTemplate.opsForHash().scan(RedisTopicKey.getClickNum.getPrefix(), ScanOptions.NONE);
        ArrayList<Topic> topics = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<String, Integer> entry = cursor.next();
            String key = entry.getKey();
            Long topicId = Long.valueOf(key);
            Integer clickNum = entry.getValue();
            Topic topic = topicService.getById(topicId);
            if (topic != null) {
                topic.setClickNum(topic.getClickNum()+clickNum);
                topics.add(topic);
            }

            redisCache.delCacheMapValue(RedisTopicKey.getClickNum, "", topicId.toString());
        }
        topicService.updateBatchById(topics);
    }
}
