package com.yhz.senbeiforummain.security.dao;


import com.yhz.senbeiforummain.security.constant.RedisRememberMeKey;
import com.yhz.senbeiforummain.util.RedisCache;

import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.Date;
import java.util.HashMap;

import java.util.Map;


/**
 * @author 吉良吉影
 */
@Component
public class RedisPersistentRepository implements PersistentTokenRepository {


    @Resource
    RedisCache redisCache;
    @Override
    public void createNewToken(PersistentRememberMeToken persistentRememberMeToken) {
        String series = persistentRememberMeToken.getSeries();
        String username = persistentRememberMeToken.getUsername();
        String tokenValue = persistentRememberMeToken.getTokenValue();
        String date = String.valueOf(persistentRememberMeToken.getDate().getTime());
        //用户只要采用账户密码重新登录，那么为了安全就有必要清除之前的token信息。deleteIfPresent方法通过
        //username查找到用户对应的series，然后删除旧的token信息。
        deleteIfPresent(username);
        HashMap<String,String > hashMap = new HashMap<>();
        hashMap.put("username",username);
        hashMap.put("token",tokenValue);
        hashMap.put("date",date);
        redisCache.setCacheMap(RedisRememberMeKey.getSeriesKey,series,hashMap);

        redisCache.setCacheObject(RedisRememberMeKey.getUsernameKey,username,series);

    }

    @Override
    public void updateToken(String s, String s1, Date date) {
        //重新登录后更新rememberme缓存中的token
        if(redisCache.hasKey(RedisRememberMeKey.getSeriesKey,s)) {
            redisCache.setCacheMapValue(RedisRememberMeKey.getSeriesKey,s,"token",s1);
        }
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String s) {
        Map<String, String> map = redisCache.getCacheMap(RedisRememberMeKey.getSeriesKey, s);
        String username =  map.get("username");
        String token = map.get("token");
        String date = map.get("date");
        if (null == username || null == token || null == date) {
            return null;
        }
        Long timestamp = Long.valueOf(date);
        Date time = new Date(timestamp);
        return new PersistentRememberMeToken(username, s, token, time);
    }

    @Override
    public void removeUserTokens(String s) {
    //rememberMeService实现类中调用这个方法传入的参数是username，因此我们必须通过username查找到
    //对应的series，然后再通过series查找到对应的token信息再删除。
        deleteIfPresent(s);
    }
    
    private void deleteIfPresent(String username){
    //删除token时应该同时删除token信息，以及保存了对应的username与series对照数据。
        if(redisCache.hasKey(RedisRememberMeKey.getUsernameKey,username)){

            String series = redisCache.getCacheObject(RedisRememberMeKey.getUsernameKey, username);
            if(series!=null && redisCache.hasKey(RedisRememberMeKey.getSeriesKey,series)){
                redisCache.deleteObject(RedisRememberMeKey.getSeriesKey, series);
                redisCache.deleteObject(RedisRememberMeKey.getUsernameKey, username);
            }
        }
    }

}
