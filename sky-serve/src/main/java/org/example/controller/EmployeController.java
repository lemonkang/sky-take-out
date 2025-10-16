package org.example.controller;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.config.Result;
import org.example.dto.EmployeRegisterDto;
import org.example.entity.Category;
import org.example.entity.EmployeEntity;
import org.example.mapper.CategoryMapper;
import org.example.service.EmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class EmployeController {

    @Autowired
    private EmployeService empService;
    @Autowired
    CategoryMapper categoryMapper;

    @PostMapping("/register")
    public Result<EmployeEntity> register(@ModelAttribute @Valid EmployeRegisterDto emp,@RequestParam(required = false) MultipartFile file) {
        log.info("register employe"+emp.toString());
        EmployeEntity employeEntity ;
        byte[] bytes=null;

        try {
           if (file != null) {
               bytes = file.getBytes();
           }

            employeEntity=empService.addEmploye(emp,bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success(employeEntity);
    }
    @PostMapping("/editInfo")
    public Result<EmployeEntity> editInfo(@ModelAttribute  EmployeRegisterDto emp,@RequestParam MultipartFile file) {
     return null;
    }
    @GetMapping("/login")
    public Result<Map<String,Object>> login(@ModelAttribute @Valid EmployeRegisterDto emp){

        Map<String, Object> login = empService.login(emp.getEmployeName(), emp.getEmployePassword());

        return Result.success(login);
    }
    @GetMapping("/info/{userId}")
    public Result<EmployeEntity> info(@PathVariable Long userId){
        EmployeEntity employeEntity = empService.infoById(userId);
        return Result.success(employeEntity);
    }
    @GetMapping("/info/image")
    public Result<List<Category>> getInfo(){
        List<Category> categoryList = categoryMapper.selectList(null);
        log.info("categoryList"+categoryList.toString());
        return Result.success(categoryList);
    }
    @GetMapping("/download/{id}")
    public void download(@PathVariable Long id, HttpServletResponse response) throws IOException {
        EmployeEntity employeEntity = empService.infoById(id);
        if(employeEntity==null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String downloadName = employeEntity.getEmployeName() + ".png";
        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(downloadName, "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
//        outputStream.write(employeEntity.getAvatar());
        outputStream.flush();


    }

}
