package org.example.controller;

import jakarta.validation.Valid;
import org.example.config.Result;
import org.example.dto.CategoryDto;
import org.example.entity.Category;
import org.example.mapper.CategoryMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("category")
public class CategoryController {
    @Autowired
    CategoryMapper categoryMapper;
    @PostMapping
    public Result<Integer> addCategory(@RequestBody @Valid CategoryDto categoryDto) {
        Category category = new Category();
        System.out.println("test");
        BeanUtils.copyProperties(categoryDto, category);
        int insert = categoryMapper.insert(category);
        return Result.success(insert);
    }
}
