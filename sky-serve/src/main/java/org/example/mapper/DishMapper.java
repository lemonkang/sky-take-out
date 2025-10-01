package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.DishEntity;
import org.example.vo.DishCategoryVo;
import org.example.vo.DishVo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Mapper
public interface DishMapper extends BaseMapper<DishEntity> {
    Page<DishCategoryVo> selectDishCategory(Page<?> page,@Param("length") int length,
                                     @Param("createTime")LocalDateTime createTime
    );
    @Select({"<script>","SELECT id,name from dish where 1=1 <if test='name!=null'> `name` LIKE CONCAT('%',#{name},'%')</if>", "</script>"})
    Page<DishEntity>  selectLike(Page<DishEntity> page, @Param("name") String name);
   int updatePrice(@Param("price")BigDecimal price,@Param("name") String name);
}
