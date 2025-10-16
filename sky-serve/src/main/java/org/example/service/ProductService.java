package org.example.service;

import org.example.dto.OrderProductDto;
import org.example.entity.ProductEntity;
import org.example.vo.OrderProductVo;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Map<String, Object> orderProduct(List<OrderProductDto> productList);
    ProductEntity productMaxBuy();
}
