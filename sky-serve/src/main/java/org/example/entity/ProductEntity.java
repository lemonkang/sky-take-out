package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@TableName("product")
@Data
@Builder
public class ProductEntity {
    @TableId("id")
    private Long id;
    private String name;
    private Double stock;
    private BigDecimal price;
    @Version
    private Integer version;
}
