package com.yhz.senbeiforummain.util;

import com.baomidou.mybatisplus.extension.api.R;
import com.sun.org.apache.regexp.internal.RE;
import com.yhz.senbeiforummain.common.constant.RedisBasePrefixKey;
import com.yhz.senbeiforummain.common.constant.RedisPrefixKey;
import com.yhz.senbeiforummain.common.constant.RedisUserKey;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 吉良吉影
 */
@SuppressWarnings(value = {"unchecked", "rawtypes"})
@Component
public class RedisCache {
    public final RedisTemplate redisTemplate;

    @Autowired
    public RedisCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param redisPrefixKey 前缀接口
     * @param key            缓存的键值
     * @param value          缓存的值
     */
    public <T> void setCacheObject(RedisPrefixKey redisPrefixKey, String key, T value) {
        redisTemplate.opsForValue().set(redisPrefixKey.getPrefix() + key, value, redisPrefixKey.expireSeconds(),TimeUnit.SECONDS);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    public <T> void setCacheObject(RedisPrefixKey redisPrefixKey, String key, T value, Integer timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(redisPrefixKey.getPrefix() + key, value, timeout, timeUnit);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(RedisPrefixKey redisPrefixKey, String key, long timeout) {
        return expire(redisPrefixKey, key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(RedisPrefixKey redisPrefixKey, String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(redisPrefixKey.getPrefix() + key, timeout, unit);
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public <T> T getCacheObject(RedisUserKey redisUserKey, String key) {
        ValueOperations<String, T> operation = redisTemplate.opsForValue();
        return operation.get(redisUserKey.getPrefix() + key);
    }

    /**
     * 删除单个对象
     *
     * @param key
     */
    public boolean deleteObject(RedisPrefixKey redisPrefixKey, String key) {
        return redisTemplate.delete(redisPrefixKey.getPrefix() + key);
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     * @return
     */
    public long deleteObject(Collection collection) {
        return redisTemplate.delete(collection);
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public <T> long setCacheList(RedisPrefixKey redisPrefixKey, String key, List<T> dataList) {
        Long count = redisTemplate.opsForList().rightPushAll(redisPrefixKey.getPrefix() + key, dataList);
        return count == null ? 0 : count;
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public <T> List<T> getCacheList(RedisPrefixKey redisPrefixKey, String key) {
        return redisTemplate.opsForList().range(redisPrefixKey.getPrefix() + key, 0, -1);
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> BoundSetOperations<String, T> setCacheSet(RedisPrefixKey redisPrefixKey, String key, Set<T> dataSet) {
        BoundSetOperations<String, T> setOperation = redisTemplate.boundSetOps(redisPrefixKey.getPrefix() + key);
        Iterator<T> it = dataSet.iterator();
        while (it.hasNext()) {
            setOperation.add(it.next());
        }
        return setOperation;
    }

    /**
     * 获得缓存的set
     *
     * @param key
     * @return
     */
    public <T> Set<T> getCacheSet(RedisPrefixKey redisPrefixKey, String key) {
        return redisTemplate.opsForSet().members(redisPrefixKey.getPrefix() + key);
    }

    /**
     * 缓存Map
     *
     * @param key
     * @param dataMap
     */
    public <T> void setCacheMap(RedisPrefixKey redisPrefixKey, String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            redisTemplate.opsForHash().putAll(redisPrefixKey.getPrefix() + key, dataMap);
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key
     * @return
     */
    public <T> Map<String, T> getCacheMap(RedisPrefixKey redisPrefixKey, String key) {
        return redisTemplate.opsForHash().entries(redisPrefixKey.getPrefix() + key);
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public <T> void setCacheMapValue(RedisPrefixKey redisPrefixKey, String key, final String hKey, final T value) {
        redisTemplate.opsForHash().put(redisPrefixKey.getPrefix() + key, hKey, value);
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public <T> T getCacheMapValue(RedisPrefixKey redisPrefixKey, String key, String hKey) {
        HashOperations<String, String, T> opsForHash = redisTemplate.opsForHash();
        return opsForHash.get(redisPrefixKey.getPrefix() + key, hKey);
    }

    /**
     * 删除Hash中的数据
     *
     * @param key
     * @param hkey
     */
    public void delCacheMapValue(RedisPrefixKey redisPrefixKey, String key, final String hkey) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(redisPrefixKey.getPrefix() + key, hkey);
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public <T> List<T> getMultiCacheMapValue(RedisPrefixKey redisPrefixKey, String key, Collection<Object> hKeys) {
        return redisTemplate.opsForHash().multiGet(redisPrefixKey.getPrefix() + key, hKeys);
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public Collection<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }
}
