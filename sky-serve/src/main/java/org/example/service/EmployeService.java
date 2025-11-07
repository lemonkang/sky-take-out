package org.example.service;

import org.example.dto.EmployeRegisterDto;
import org.example.entity.EmployeEntity;

import java.util.Map;

public interface EmployeService {
    EmployeEntity addEmploye(EmployeRegisterDto emp,byte[] avatar);
    EmployeEntity editInfo(EmployeRegisterDto emp,byte[] avatar);
    String login(String employeName, String employePassword );
    EmployeEntity infoById(Long id);
}
