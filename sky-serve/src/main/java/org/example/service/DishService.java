package org.example.service;

import org.example.dto.DishDto;
import org.example.entity.DishEntity;

public interface DishService {
    DishEntity addDish(DishDto dish);
}
