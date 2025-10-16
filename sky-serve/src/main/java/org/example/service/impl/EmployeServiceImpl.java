package org.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.config.JwtContext;
import org.example.dto.EmployeRegisterDto;
import org.example.entity.EmployeEntity;
import org.example.mapper.EmployeMapper;
import org.example.service.EmployeService;
import org.example.util.BeanCopyUtil;
import org.example.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
@CacheConfig(cacheNames = "userCache")
public class EmployeServiceImpl implements EmployeService {
    @Autowired
    private EmployeMapper empMapper;
    @Autowired
    JwtContext jwtContext;
    @Autowired
    JwtUtil jwtUtil;

    @CachePut(key = "#emp.employeName")
    @Override
    public EmployeEntity addEmploye(EmployeRegisterDto emp,byte[] avatar) {
        EmployeEntity employeEntity = new EmployeEntity();
        BeanUtils.copyProperties(emp,employeEntity );

        if (avatar!=null&&avatar.length>0) {
            String base64 = Base64.getEncoder().encodeToString(avatar);

            employeEntity.setAvatar(base64);
        }
        empMapper.insert(employeEntity);
        return employeEntity;
    }

    @Override
    public EmployeEntity editInfo(EmployeRegisterDto emp, byte[] avatar) {
        EmployeEntity oldEmployeEntity = (EmployeEntity)jwtContext.getJwtContext();
        BeanCopyUtil.copyNonNullProperties(emp,oldEmployeEntity);
        if (avatar!=null&&avatar.length>0) {
            String base64 = Base64.getEncoder().encodeToString(avatar);
            oldEmployeEntity.setAvatar(base64);
        }
        empMapper.updateById(oldEmployeEntity);
        return oldEmployeEntity;
    }

    @Override
    @Cacheable(key = "#employeName")
    public Map<String,Object> login(String employeName, String employePassword) {
        EmployeEntity employeEntity = empMapper.selectOne(new QueryWrapper<EmployeEntity>().eq("employe_name", employeName));
        if (employeEntity == null) {
            return null;
        }
        if (employePassword.equals(employeEntity.getEmployePassword())) {
            String token = jwtUtil.generateToken(employeName);
            return Map.of("token", token,"employeEntity", employeEntity);
        }
        return null;
    }

    @Override
    public EmployeEntity infoById(Long id) {
        EmployeEntity employeEntity = empMapper.selectById(id);
        return employeEntity;
    }
}
