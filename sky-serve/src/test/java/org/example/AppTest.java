package org.example;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.example.entity.EmployeEntity;
import org.example.entity.OrdersEntity;
import org.example.mapper.ProductMapper;
import org.example.property.JwtProperty;
import org.example.util.BeanCopyUtil;
import org.example.vo.DishVo;
import org.example.vo.OrderProductVo;
import org.glassfish.jaxb.core.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.hash.HashMapper;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@SpringBootTest
public class AppTest {
    @Autowired
    RedisTemplate redisTemplate;
    @Test
    public void contextLoads() {
        redisTemplate.opsForHash().put("person","name","inno");
        Object o = redisTemplate.opsForHash().get("person", "name");
        System.out.println(o);
    }
    @Test
    public void test1(){

        Map animal = redisTemplate.opsForHash().entries("Animal");
        System.out.println(JSON.toJSONString(animal));


    }
    @Test
    public void test2(){
        DishVo build = DishVo.builder().id(12L).dishName("111").build();
        DishVo buildTwo = DishVo.builder().id(11L).dishName("222").build();
        redisTemplate.opsForHash().put("Food","one",JSON.toJSONString(build));
        redisTemplate.opsForHash().put("Food","two",JSON.toJSONString(buildTwo));
    }
    @Test
    public void test3(){
        Map<String,String> food = redisTemplate.opsForHash().entries("Food");

       Set<Map.Entry<String,String>> sets= food.entrySet();
       for(Map.Entry<String,String> entry:sets){
           String value = entry.getValue();
           Object parse = JSON.parse(value);
           if (parse instanceof String){
               System.out.println("string");
           }
           if (parse instanceof DishVo){
               System.out.println("DishVo");
           }
           System.out.println(parse.getClass());
       }
//       DishVo vo= (DishVo) JSON.parse( entry.getValue());
//           System.out.println(vo.getClass());
//
//       }
    }
    @Test
    public void test4(){


        DishVo build = DishVo.builder().id(12L).dishName("111").build();
        String jsonString = JSON.toJSONString(build);
// 正确方式
        DishVo parse = JSON.parseObject(jsonString, DishVo.class);

        System.out.println(parse.getClass()); // class com.example.DishVo
        System.out.println(parse.getDishName()); // 111

    }
    @Test
    public void test5() throws JsonProcessingException {
        DishVo build = DishVo.builder().id(12L).dishName("111").build();
        ObjectMapper objectMapper = new ObjectMapper();
        String string = objectMapper.writeValueAsString(build);
        DishVo dishVo = objectMapper.readValue(string, DishVo.class);
        System.out.println(dishVo);


    }
    @Test
    public void test6(){
        EmployeEntity buildOld = EmployeEntity.builder().id(12L).employeName("111").employePassword("222").avatar("stasd".getBytes(StandardCharsets.UTF_8)).build();
        EmployeEntity buildNew = EmployeEntity.builder().employeName("222").build();
        String string = Objects.toString(buildNew);
        int start = string.indexOf("(");
        String substring = string.substring(start);
        String replaceAll = substring.replaceAll("[),(]", "");
        String[] strings = replaceAll.split(" ");
        Map<String, String> stringStringMap = Arrays.stream(strings).filter(item -> !item.contains("null")).collect(Collectors.toMap(item -> item.split("=")[0], item -> item.split("=")[1]));
       Set<Map.Entry<String,String>> sets= stringStringMap.entrySet();
       for(Map.Entry<String,String> entry:sets){
           String[] strings1 = entry.getKey().split("");
           strings1[0]= strings1[0].toUpperCase();
           String newKey = Arrays.stream(strings1).collect(Collectors.joining());
           System.out.println(newKey);


           String value = entry.getValue();

       }
        System.out.println(stringStringMap);

    }
    @Test
    public void test7(){
        EmployeEntity buildOld = EmployeEntity.builder().id(12L).employeName("111").employePassword("222").avatar("stasd".getBytes(StandardCharsets.UTF_8)).build();
        EmployeEntity buildNew = EmployeEntity.builder().employeName("222").build();
        BeanCopyUtil.copyNonNullProperties(buildNew,buildOld);
        System.out.println(buildOld);
        System.out.println(buildNew);

    }










}
