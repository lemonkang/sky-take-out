package org.example;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.apache.ibatis.executor.BatchResult;
import org.example.config.PracticeDemo;
import org.example.dto.DishDto;
import org.example.entity.DishEntity;
import org.example.entity.EmployeEntity;
import org.example.mapper.CategoryMapper;
import org.example.mapper.DishMapper;
import org.example.mapper.EmployeMapper;
import org.example.property.JwtProperty;
import org.example.property.OssProperty;
import org.example.service.EmployeService;
import org.example.util.JwtUtil;
import org.example.util.OssUtil;
import org.example.vo.DishCategoryVo;
import org.example.vo.DishVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@SpringBootTest
public class AppTest {
    @Autowired
    DataSource dataSource;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    JwtProperty jwtProperty;
    @Autowired
    EmployeMapper employeMapper;
    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    EmployeService employeService;

    @Autowired
    OssProperty ossProperty;
    @Autowired
    OssUtil ossUtil;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    PracticeDemo practiceDemo;
    @Test
    public void test18(){
        System.out.println(practiceDemo);
    }



    @Test
    public void contextLoads() throws ClassNotFoundException, SQLException {
       Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sky_take", "root", "root");
        System.out.println(connection);

    }
    @Test
    public void test02() {
        UpdateWrapper<DishEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("create_time", LocalDateTime.now());
       updateWrapper.isNull("create_time");
        int update = dishMapper.update(updateWrapper);
        System.out.println(update);


    }
    @Test
    public void test03() {
        QueryWrapper<DishEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("name","price","create_time");
        List<DishEntity> list = dishMapper.selectList(queryWrapper);
        System.out.println(list);
    }
    @Test
    public void test04() {
        BigDecimal a = new BigDecimal("0.1");
        BigDecimal b = new BigDecimal("0.2");
        BigDecimal add = a.add(b);
        BigDecimal subtract = a.subtract(b);
        BigDecimal multiply = a.multiply(b);
        BigDecimal divide = a.divide(add, 2, BigDecimal.ROUND_HALF_UP);
        System.out.println(divide);
        System.out.println(multiply);
        System.out.println(subtract);
        System.out.println(add);
        System.out.println(0.1+0.2);
    }
    @Test
    public void test05() {
        long start = Instant.now().toEpochMilli();
        long page=2L;
        long pageSize=10L;
        Page<DishEntity> dishEntityPage = new Page<>(page,pageSize);

        QueryWrapper<DishEntity> queryWrapper = new QueryWrapper<DishEntity>();
        Page<DishEntity> entityPage = dishMapper.selectPage(dishEntityPage, queryWrapper);
        System.out.println(entityPage);
        long end = Instant.now().toEpochMilli();
        System.out.println("subtractTime:"+(end-start));

    }
    @Test
    public void test06() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        Set<Map.Entry<String, Integer>> entries = hashMap.entrySet();
        for(Map.Entry<String,Integer> entry:entries){
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }
    @Test
    public void test07() {
        String dateStr="2022年6月5日 11时2分22秒";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年M月d日 H时m分s秒");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, dateTimeFormatter);
        Integer page=2;
        Integer pageSize=10;
        Page<DishCategoryVo> objectPage = new Page<>(page, pageSize);
        Page<DishCategoryVo> dishCategoryVos = dishMapper.selectDishCategory(objectPage,5, localDateTime);
        System.out.println(dishCategoryVos);

    }
    @Test
    public void test08(){
        long page=1L;
        long pageSize=10L;
        Page<DishEntity> dishEntityPage = new Page<>(page,pageSize);
        Page<DishEntity> dishEntities = dishMapper.selectLike(dishEntityPage,null);
        System.out.println(dishEntities);
        System.out.println(dishEntities.getRecords().size());
    }
    @Test
    public void test09(){

        DishDto dishDto = DishDto.builder().price(BigDecimal.valueOf(12)).build();
        QueryWrapper<DishEntity> dishQuery = new QueryWrapper<>();
        if (dishDto.getImg()!=null){
            dishQuery.eq("img",dishDto.getImg());
        }
        if (dishDto.getName()!=null){
            dishQuery.apply("name like '_"+dishDto.getName()+"%'");
        }
        if (dishDto.getPrice()!=null){
            dishQuery.gt("price",dishDto.getPrice());
        }
        dishQuery.select("id","name","price");
        Page<DishEntity> dishEntityPage = new Page<>(1, 10);
        Page<DishEntity> dishEntityPage1 = dishMapper.selectPage(dishEntityPage, dishQuery);
        System.out.println(dishEntityPage1);
    }
    @Test
    public void test10(){
        QueryWrapper<DishEntity> wrapper = new QueryWrapper<>();
        wrapper.lt("id",10);
        wrapper.eq(false,"name","inno");
        wrapper.select("id","name");
        List<DishEntity> dishEntities = dishMapper.selectList(wrapper);
        dishEntities.stream().forEach(item->item.setName(item.getName()+"小姐") );
        dishMapper.updateById(dishEntities);
        System.out.println(dishEntities);
    }
    @Test
    public void test11(){
        int i = dishMapper.updatePrice(BigDecimal.valueOf(12), "小姐");
        System.out.println(i);
    }
    @Test
    public void test12(){
        UUID uuid = UUID.randomUUID();
        QueryWrapper<DishEntity> queryWrapper = new QueryWrapper<DishEntity>().lt("id", 10);
        List<DishEntity> dishEntities = dishMapper.selectList(queryWrapper);
        dishEntities.stream().forEach(item->{
            long randomId=(int)Math.floor(Math.random()*100) +600+Instant.now().toEpochMilli();

            item.setId( randomId);
            item.setName(item.getName()+"批量增加");
        });
        List<BatchResult> insert = dishMapper.insert(dishEntities);


    }
    @Test
    public void test13(){
        List<EmployeEntity> list = new ArrayList<>();

        for(int i=12;i<20;i++){
            EmployeEntity employe = EmployeEntity.builder().employeName("employeName"+i).employePassword("employePassword"+i).build();
           list.add(employe);
        }
        int i = employeMapper.insertEmployeeBatch(list);
        System.out.println(i);
    }
    @Test
    public void test14(){
        QueryWrapper<EmployeEntity> createTime = new QueryWrapper<EmployeEntity>().isNull("create_time");
        List<Long> longStream = employeMapper.selectList(createTime).stream().map(item -> item.getId()).toList();
        int i = employeMapper.deleteByEmployeeKey(longStream);
        System.out.println(i);

    }
    @Test
    public void test15(){
        QueryWrapper<EmployeEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("employe_name REGEXP {0}","^[a-zA-Z].{0,}[0-9]{2}$");
        List<EmployeEntity> list = employeMapper.selectList(queryWrapper);
        System.out.println(list);
//        list.stream().forEach(item->{
//            item.setCreateTime(Instant.now().atZone(ZoneId.systemDefault()).toLocalDateTime());
//            item.setEmployeName("修改"+item.getEmployeName());
//            item.setEmployePassword("修改"+item.getEmployePassword());
//
//        });
//        list.forEach(item->{
//            employeMapper.updateById(item);
//        });
//        System.out.println(list);
//        int i = employeMapper.updateByEmployeeKey(list);
//        System.out.println(i);
//        System.out.println(list);

    }
    @Test
    public void test16(){
        BigDecimal bigDecimal = BigDecimal.valueOf(12);
        BigDecimal multiply = bigDecimal.multiply(BigDecimal.valueOf(2.2));
        System.out.println(multiply);

    }





}
