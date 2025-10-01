package org.example.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DishDto {
    @Size(max = 5,message = "最长不超过五")
    private String name;
    private String description;
    @Min(0)
    private BigDecimal price;
    private Long categoryId;
    private String img;
}
