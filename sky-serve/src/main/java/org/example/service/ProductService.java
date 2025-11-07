package org.example.service;

import org.example.dto.OrderProductDto;
import org.example.dto.ProductDto;
import org.example.dto.UpdateStockDto;
import org.example.entity.ProductEntity;
import org.example.exception.DeficiencyException;
import org.example.vo.OrderProductVo;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Map<String, Object> orderProduct(List<OrderProductDto> productList) throws DeficiencyException;
    ProductEntity getProductById(String id);
    Boolean updateProduct(ProductEntity product);
    Boolean buyProduct(ProductDto productDto);
    Boolean updateStock(UpdateStockDto updateStockDto);
}
