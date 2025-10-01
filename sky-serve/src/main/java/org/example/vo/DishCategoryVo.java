package org.example.vo;

import lombok.Data;

import java.util.List;

@Data
public class DishCategoryVo {
    private Long categoryId;
    private String categoryName;
    private List<DishVo> dishList;

}
