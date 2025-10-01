package org.example.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class PracticeDemo {
    @Value("${jwt.jwt-key}")
    private String token;
    private String name;
    public PracticeDemo() {
        System.out.println("构造函数");
    }
    @PostConstruct
    void init(){
        System.out.println("init数据");
        name=token;
    }
}
