package com.cloud.photo.api.utils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RedisServiceImpl {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    private static  final String USER_CACHE_PRE = "u_c_";
    /**
     * 设置缓存*
     * @param userId
     * @param userName
     */
    public  void setUser2Cache(Long userId,String userName){

        ValueOperations<String, String> valueOps  = stringRedisTemplate.opsForValue();
        String cacheKey = USER_CACHE_PRE+userId;
        valueOps.set(cacheKey,userName);
    }
    /**
     * 查询缓存*
     * @param userId
     * @return
     */
    public String getUserCache(Long userId){
        ValueOperations<String, String> valueOps  = stringRedisTemplate.opsForValue();
        String cacheKey = USER_CACHE_PRE+userId;
        String userName = valueOps.get(cacheKey);
        return userName;
    }




}
