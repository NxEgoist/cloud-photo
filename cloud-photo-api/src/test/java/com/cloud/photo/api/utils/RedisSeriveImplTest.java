package com.cloud.photo.api.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisSeriveImplTest {

    @Autowired
    private RedisServiceImpl redisService;

    @Test
    public  void setUser2Cache() {
        redisService.setUser2Cache(888888l, "wangwu");
    }


    @Test
    public void getUserCache() {
        long beginTime = System.currentTimeMillis();
        String userName = "";
        for (int i = 0; i <= 10000; i++) {
            userName = redisService.getUserCache(888888L);
        }
        System.out.println("getUserCache=" + userName + ",get 10000 cost:" + (System.currentTimeMillis() - beginTime) + "毫秒");
    }
}
