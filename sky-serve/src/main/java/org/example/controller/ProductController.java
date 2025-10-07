package org.example.controller;

import jakarta.validation.Valid;
import org.example.config.Result;
import org.example.dto.OrderProductDto;
import org.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;
    @PostMapping("orderProduct")
    private Result<Map<String,Object>> orderProduct(@Valid @RequestBody List<OrderProductDto> productList){
        Map<String, Object> stringObjectMap = productService.orderProduct(productList);

        return Result.success(stringObjectMap);
    }
}
