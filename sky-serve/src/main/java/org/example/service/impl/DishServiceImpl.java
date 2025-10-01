package org.example.service.impl;

import org.example.dto.DishDto;
import org.example.entity.DishEntity;
import org.example.mapper.DishMapper;
import org.example.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;

    @Override
    @Transactional
    public DishEntity addDish(DishDto dish) {
        DishEntity dishEntity = new DishEntity();
        BeanUtils.copyProperties(dish, dishEntity);
        dishMapper.insert(dishEntity);

        return dishEntity;
    }
}
