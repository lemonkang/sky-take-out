package org.example.util;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class LimitIp {
    private Integer limiMaxCount=10;
    private Integer expireTime=1;

    @Autowired
    RedisTemplate redisTemplate;

    public  boolean limitIp(String ip) {
        Long increment = redisTemplate.opsForValue().increment(ip, 1);
        if (increment==1){
            redisTemplate.expire(ip,expireTime, TimeUnit.MINUTES);
        }

        return increment >= limiMaxCount;
    }


}
