package org.example.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.example.entity.EmployeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// java example
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Autowired
    JwtContext jwtContext;
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填充...");
        EmployeEntity employeEntity = (EmployeEntity)jwtContext.getJwtContext();
        if (employeEntity != null) {
            this.strictInsertFill(metaObject, "createUser", Long.class, employeEntity.getId());
            this.strictInsertFill(metaObject, "updateUser", Long.class, employeEntity.getId());
        }

        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填充...");
        EmployeEntity employeEntity = (EmployeEntity)jwtContext.getJwtContext();
        if (employeEntity != null) {
            this.strictInsertFill(metaObject, "updateUser", Long.class, employeEntity.getId());
        }

        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}