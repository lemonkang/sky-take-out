package org.example.controller;

import jakarta.validation.Valid;
import org.example.config.Result;
import org.example.dto.DishDto;
import org.example.entity.DishEntity;
import org.example.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @PostMapping
    public Result<DishEntity> add(@RequestBody @Valid DishDto dishDto){
        DishEntity dishEntity = dishService.addDish(dishDto);
        return Result.success(dishEntity);
    }

}
