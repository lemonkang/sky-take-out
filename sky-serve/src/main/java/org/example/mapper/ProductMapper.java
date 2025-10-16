package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.entity.ProductEntity;
import org.example.vo.OrderProductVo;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductMapper extends BaseMapper<ProductEntity> {
    Map<String,Object> productMaxBuy();
    List<OrderProductVo> orderList(@Param("userId") Long userId);
}
