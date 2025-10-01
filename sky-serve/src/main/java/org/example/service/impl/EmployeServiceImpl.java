package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.EmployeRegisterDto;
import org.example.entity.EmployeEntity;
import org.example.mapper.EmployeMapper;
import org.example.service.EmployeService;
import org.example.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service

public class EmployeServiceImpl implements EmployeService {
    @Autowired
    private EmployeMapper empMapper;
    @Autowired
    JwtUtil jwtUtil;
    @Override
    public EmployeEntity addEmploye(EmployeRegisterDto emp,byte[] avatar) {
        EmployeEntity employeEntity = new EmployeEntity();
        BeanUtils.copyProperties(emp, employeEntity);

        if (avatar!=null&&avatar.length>0) {
            employeEntity.setAvatar(avatar);
        }
        empMapper.insert(employeEntity);
        return employeEntity;
    }

    @Override
    public String login(String employeName, String employePassword) {
        EmployeEntity employeEntity = empMapper.selectOne(new QueryWrapper<EmployeEntity>().eq("employe_name", employeName));
        if (employeEntity == null) {
            return null;
        }
        if (employePassword.equals(employeEntity.getEmployePassword())) {
            String token = jwtUtil.generateToken(employeName);
            return token;
        }
        return "";
    }

    @Override
    public EmployeEntity infoById(Long id) {
        EmployeEntity employeEntity = empMapper.selectById(id);
        return employeEntity;
    }
}
