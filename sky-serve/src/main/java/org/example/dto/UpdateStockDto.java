package org.example.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStockDto {
    @NotNull(message = "产品ID不能为空")
    private Long productId;
    
    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存数量不能小于0")
    private Double stock;
    
    @NotNull(message = "版本号不能为空")
    private Integer version;
}

