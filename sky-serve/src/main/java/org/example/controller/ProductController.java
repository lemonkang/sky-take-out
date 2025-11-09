package org.example.controller;

import jakarta.validation.Valid;
import org.example.config.RabbitConfig;
import org.example.config.Result;
import org.example.dto.OrderProductDto;
import org.example.dto.ProductDto;
import org.example.dto.SeckillMessage;
import org.example.dto.UpdateStockDto;
import org.example.entity.ProductEntity;
import org.example.exception.DeficiencyException;
import org.example.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @PostMapping("orderProduct")
    private Result<Map<String,Object>> orderProduct(@Valid @RequestBody List<OrderProductDto> productList) throws DeficiencyException {
        Map<String, Object> stringObjectMap = productService.orderProduct(productList);

        return Result.success(stringObjectMap);
    }
    @PostMapping("/message")
    public void message(){
        for (int i = 0; i < 10; i++) {
            SeckillMessage seckillMessage = new SeckillMessage(Long.valueOf(i), Long.valueOf(i));
            rabbitTemplate.convertAndSend(
                    RabbitConfig.Skill_EXCHANGE,
                    RabbitConfig.Skill_ROUTING_KEY,
                    seckillMessage
            );
        }
    }
    @GetMapping("/{id}")
    public Result<ProductEntity> getProduct(@PathVariable String id) {



        ProductEntity product = productService.getProductById(id);
        return Result.success(null);
    }

    @PutMapping
    public Result<Boolean> updateProduct(@RequestBody ProductEntity product) {
        boolean success = productService.updateProduct(product);
        return Result.success(success);
    }
    @PostMapping("/buyProduct")
    public Result<Boolean> buyProduct(@RequestBody ProductDto product) {
        Boolean b = productService.buyProduct(product);
        return Result.success(b);
    }

    @PutMapping("/stock")
    public Result<Boolean> updateStock(@Valid @RequestBody UpdateStockDto updateStockDto) {
        Boolean success = productService.updateStock(updateStockDto);
        if (success) {
            return Result.success(true);
        } else {
            return Result.error("更新失败，可能数据已被其他操作修改，请刷新后重试");
        }
    }

}
