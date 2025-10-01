package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("employe")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String employeName;
    private String employePassword;
    private byte[] avatar;
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm:ss")
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy年MM月dd日 HH:mm:ss")
    private LocalDateTime updateTime;
}
