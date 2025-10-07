package org.example.vo;

import lombok.Data;
import org.example.entity.OrderDetailEntity;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderProductVo {
    private Long orderId;
    private BigDecimal totalAmount;
    private List<OrderDetailEntity> orderDetails;
}
