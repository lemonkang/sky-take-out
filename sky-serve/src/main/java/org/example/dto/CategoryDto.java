package org.example.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.constant.CategoryConstantStatus;
import org.example.constant.TaskStatusEnum;
import org.example.constant.Type;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private String name;
    @NotNull(message = "sort 不能为空")
    private Integer sort;
    private Integer status= CategoryConstantStatus.ON;
    private Type type;
    @JsonFormat(pattern = "yyyy年M月d日")
    private LocalDate productTime;
}
