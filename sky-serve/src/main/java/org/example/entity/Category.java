package org.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.constant.CategoryConstantStatus;
import org.example.constant.TaskStatusEnum;
import org.example.constant.Type;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("category")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
//    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String name;
    private Integer sort;
    private Integer status= CategoryConstantStatus.ON;

    private Type type;

    private Time productTime;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
