package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import jakarta.annotation.Resource;
import org.example.config.JwtContext;
import org.example.dto.OrderProductDto;
import org.example.entity.EmployeEntity;
import org.example.entity.OrderDetailEntity;
import org.example.entity.OrdersEntity;
import org.example.entity.ProductEntity;
import org.example.exception.DeficiencyException;
import org.example.mapper.OrderDetailMapper;
import org.example.mapper.OrdersMapper;
import org.example.mapper.ProductMapper;
import org.example.service.ProductService;
import org.example.vo.OrderProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    @Resource
    JwtContext<EmployeEntity> jwtContext;
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Override
    @Transactional
    public Map<String, Object> orderProduct(List<OrderProductDto> productList) {
        EmployeEntity currentUser = jwtContext.getJwtContext();

        BigDecimal totalAmount = productList.stream().map(item -> item.getProductPrice().multiply(new BigDecimal(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add);

        OrdersEntity ordersEntity = OrdersEntity.builder().userId(currentUser.getId()).totalAmount(totalAmount).build();

        ordersMapper.insert(ordersEntity);
        List<Long> idList = productList.stream().map(OrderProductDto::getProductId).toList();
        List<ProductEntity> productEntityList = productMapper.selectByIds(idList);
        List<OrderProductDto> deficiencyList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            ProductEntity productEntity = productEntityList.get(i);
            OrderProductDto orderProductDto = productList.get(i);
            if (orderProductDto.getQuantity()>productEntity.getStock()){
                deficiencyList.add(orderProductDto);
            }
        }

        if (!deficiencyList.isEmpty()){
            throw new DeficiencyException("库存不足",deficiencyList);
        }
        for (int i = 0; i < productList.size(); i++) {
            ProductEntity productEntity = productEntityList.get(i);
            OrderProductDto orderProductDto = productList.get(i);
            Double newStock= productEntity.getStock()-orderProductDto.getQuantity();
            UpdateWrapper<ProductEntity> updateWrapper = new UpdateWrapper<ProductEntity>().set("stock", newStock).set("version", productEntity.getVersion() + 1)
                    .eq("id", productEntity.getId()).eq("version", productEntity.getVersion());
            try {
                //模拟耗时操作
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            productMapper.update(updateWrapper);
            OrderDetailEntity orderDetailEntity = OrderDetailEntity.builder().orderId(ordersEntity.getId()).productId(productEntity.getId()).quantity(orderProductDto.getQuantity())
                    .snapShotPrice(orderProductDto.getProductPrice()).build();
            orderDetailMapper.insert(orderDetailEntity);
        }

        return new HashMap<>(Map.of("order", ordersEntity, "productList", productList));
    }

    @Override
    public ProductEntity productMaxBuy() {

        return null;
    }
}
