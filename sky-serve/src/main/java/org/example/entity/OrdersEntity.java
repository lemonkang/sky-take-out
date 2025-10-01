package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersEntity {
    @TableId(value ="order_id",type = IdType.ASSIGN_ID)
    private Long orderId;
    private Long userId;
    private Long productId;
    private Double quentity;
    @TableField(fill = FieldFill.INSERT )
    private LocalDateTime createTime;

}
