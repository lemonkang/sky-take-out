package org.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@TableName("order_detail")
@Data
@Builder
public class OrderDetailEntity {
    @TableId(type = IdType.ASSIGN_ID )
    private Long id;
    private Long orderId;
    private Long productId;
    private BigDecimal snapShotPrice;
    private Integer quantity;
}
