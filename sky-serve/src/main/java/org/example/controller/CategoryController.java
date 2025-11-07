package org.example.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.config.Result;
import org.example.dto.CategoryDto;
import org.example.entity.Category;
import org.example.mapper.CategoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    CategoryMapper categoryMapper;
    @PostMapping
    public Result<Integer> addCategory(@RequestBody @Valid CategoryDto categoryDto, @RequestParam @DateTimeFormat(pattern = "yyyy年M月dd日 H时m分s秒") LocalDateTime createTime) {
        log.info(createTime+"=====控制层  addCategory===="+categoryDto);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object userInfo =  auth.getPrincipal();
        log.info("安全"+userInfo);

        Category category = new Category();
        System.out.println("test");
        BeanUtils.copyProperties(categoryDto, category);
        int insert = categoryMapper.insert(category);
        return Result.success(insert);
    }
}
