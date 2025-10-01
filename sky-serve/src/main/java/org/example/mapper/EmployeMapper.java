package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.entity.EmployeEntity;

import java.util.List;

@Mapper
public interface EmployeMapper extends BaseMapper<EmployeEntity> {
    EmployeEntity selectByEmployeeKey(@Param("id") Integer id);
    int insertEmployee(EmployeEntity employee);
    int insertEmployeeBatch(@Param("employeeList") List<EmployeEntity> employeeList);
    int deleteByEmployeeKey(@Param("ids") List<Long> ids);
    int updateByEmployeeKey(@Param("employeeList") List<EmployeEntity> employeeList);
}
