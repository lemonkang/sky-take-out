package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class EmployeRegisterDto {
    @NotBlank(message = "employeName not blank")
    private String employeName;
    @NotBlank(message = "employePassword not blank")
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$",
//            message = "密码至少6位，且必须包含字母和数字")
    private String employePassword;
}
