package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jakarta.annotation.Resource;
import org.example.config.JwtContext;
import org.example.dto.OrderProductDto;
import org.example.dto.ProductDto;
import org.example.dto.UpdateStockDto;
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
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Resource
    JwtContext<EmployeEntity> jwtContext;
    @Autowired
    OrdersMapper ordersMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderDetailMapper orderDetailMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedissonClient redissonClient;

    @Override
    @Transactional
    public Map<String, Object> orderProduct(List<OrderProductDto> productList) throws DeficiencyException {
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
    public ProductEntity getProductById(String id) {
        Object object = redisTemplate.opsForValue().get(id);
       if (object != null) {
           return (ProductEntity)object;
       }
        ProductEntity productEntity = productMapper.selectById(id);
       redisTemplate.opsForValue().set(id, productEntity);
       return productEntity;

    }

    @Override
    @Transactional
    public Boolean updateProduct(ProductEntity product) {
        productMapper.updateById(product);
        redisTemplate.delete(String.valueOf( product.getId()));
//        Object str="asdf";
//        Integer a=(Integer) str;
        return false;
    }

    @Override
    @Transactional
    public Boolean buyProduct(ProductDto productDto) {

            String lockKey = "product:stock:lock:" + productDto.getProductId();
            RLock lock = redissonClient.getLock(lockKey);
            boolean locked = false;
            try {
                locked = lock.tryLock(5, 10, TimeUnit.SECONDS);
                if (!locked) {
                    log.warn("Failed to acquire lock for product {}", productDto.getProductId());
                    return false;
                }

                ProductEntity productEntity = productMapper.selectById(productDto.getProductId());
                if (productEntity == null) {
                    log.warn("Product {} not found", productDto.getProductId());
                    return false;
                }

                if (productDto.getProductNum() > productEntity.getStock()) {
                    return false;
                }

                Double newStock = productEntity.getStock() - productDto.getProductNum();
                LambdaUpdateWrapper<ProductEntity> lambdaUpdateWrapper = Wrappers.<ProductEntity>lambdaUpdate()
                        .set(ProductEntity::getStock, newStock)
                        .eq(ProductEntity::getId, productEntity.getId());

                int update = productMapper.update(lambdaUpdateWrapper);
                return update > 0;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            } finally {
                if (locked && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }

    }

    @Override
    @Transactional
    public Boolean updateStock(UpdateStockDto updateStockDto) {
        // 先查询当前产品信息，获取最新的version
        ProductEntity productEntity = productMapper.selectById(updateStockDto.getProductId());
        if (productEntity == null) {
            return false;
        }
        
        // 使用乐观锁更新库存：只有在version匹配时才能更新成功
        UpdateWrapper<ProductEntity> updateWrapper = new UpdateWrapper<ProductEntity>()
                .set("stock", updateStockDto.getStock())
                .set("version", updateStockDto.getVersion() + 1)
                .eq("id", updateStockDto.getProductId())
                .eq("version", updateStockDto.getVersion());
        
        int updateCount = productMapper.update(updateWrapper);
        
        // 如果更新成功，清除Redis缓存
        if (updateCount > 0) {
            redisTemplate.delete(String.valueOf(updateStockDto.getProductId()));
            return true;
        }
        
        // 更新失败，说明version不匹配（数据已被其他线程修改）
        return false;
    }


}
