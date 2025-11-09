package org.example;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.config.RabbitConfig;
import org.example.entity.EmployeEntity;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest
public class AppTest {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    RabbitTemplate rabbitTemplate;


    @Test
    public void test1(){




    }
    @Test
    public void test2(){
        EmployeEntity build = EmployeEntity.builder().id(999L).employeName("employeName").employePassword("password").build();
         redisTemplate.opsForValue().set("Json2",build);
    }
    @Test
    public void test3(){
        EmployeEntity build = EmployeEntity.builder().id(999L).employeName("employeName").employePassword("password").build();
        redisTemplate.opsForValue().set("Json2",build);
        EmployeEntity object2 = (EmployeEntity)redisTemplate.opsForValue().get("Json2");
        System.out.println(object2);

    }
    @Test
    public void test04(){
         redisTemplate.opsForValue().increment("increment",1);
        redisTemplate.opsForValue().increment("increment",1);
        Long count = redisTemplate.opsForValue().increment("increment", 1);
        redisTemplate.expire("increment",1,TimeUnit.MINUTES);

    }
    @Test
    public void test05(){
        AtomicInteger atomicInteger = new AtomicInteger(10);
        atomicInteger.addAndGet(1);
        System.out.println(atomicInteger.get());


    }


}
