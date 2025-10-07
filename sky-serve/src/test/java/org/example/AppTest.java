package org.example;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.OrderProductDto;
import org.example.property.JwtProperty;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@SpringBootTest
public class AppTest {
    @Resource
    JwtProperty jwtProperty;


    @Test
    public void contextLoads() {
        Integer a=10;
        BigDecimal b=BigDecimal.valueOf(a);
        System.out.println(b);
    }






}
