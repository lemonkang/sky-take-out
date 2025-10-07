package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductDto {
    private Long productId;
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.00", inclusive = false, message = "价格必须大于 0")
    @Digits(integer = 10, fraction = 2, message = "价格格式不正确，最多保留两位小数")
    private BigDecimal productPrice;
    private Integer quantity;
}
