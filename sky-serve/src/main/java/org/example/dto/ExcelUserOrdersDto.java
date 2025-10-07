package org.example.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExcelUserOrdersDto {
    @ExcelProperty("用户名")
    private String employeName;
    @ExcelProperty("密码")
    private String employePassword;
    @ExcelProperty("产品ID")
    private Long productId;
    @ExcelProperty("数量")
    private Double quantity;
    @ExcelProperty(value = "订单日期" )
//    @JsonFormat(pattern = "yyyy年M月d天")
    @DateTimeFormat("yyyy年M月d天")
    private LocalDate orderDate;
}
