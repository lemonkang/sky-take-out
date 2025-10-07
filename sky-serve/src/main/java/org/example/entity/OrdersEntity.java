package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersEntity {
    @TableId(value ="id",type = IdType.ASSIGN_ID)
    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
}
