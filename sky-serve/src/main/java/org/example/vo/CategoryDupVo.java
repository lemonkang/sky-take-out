package org.example.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDupVo {
    private Integer num;
    private String dupId;
    private String name;
    private String createTime;
}
