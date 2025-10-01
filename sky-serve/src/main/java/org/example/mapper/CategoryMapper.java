package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.dto.CategoryDto;
import org.example.entity.Category;
import org.example.vo.CategoryDupVo;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    Integer insertCategory(CategoryDto category);
    List<CategoryDupVo> selectCategoryDup();
}
