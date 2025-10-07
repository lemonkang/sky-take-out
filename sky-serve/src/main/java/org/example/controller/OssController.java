package org.example.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.config.NoticeRowWriteHandler;
import org.example.dto.ExcelUserOrdersDto;
import org.example.service.UploadFileService;
import org.example.util.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/oss")
@Slf4j
public class OssController {

    @Autowired
    OssUtil ossUtil;
    @Autowired
    UploadFileService uploadFileService;

    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile imgFile) {
        String fileName = imgFile.getName();
        try {
            InputStream inputStream = imgFile.getInputStream();
           return ossUtil.upload(inputStream, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @PostMapping("/uploadExcel")
    public int uploadExcel(@RequestParam("excelFile") MultipartFile file) {
        int i;
        try {
             i = uploadFileService.uploadExcel(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return i;
    }
    @GetMapping("/getExcel")
    public void getExcel(HttpServletResponse response) throws IOException {
        log.info("getExcel");
        List<ExcelUserOrdersDto> list=new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            ExcelUserOrdersDto excelUserOrdersDto=new ExcelUserOrdersDto();
            excelUserOrdersDto.setOrderDate(LocalDate.now());
            excelUserOrdersDto.setEmployeName("用户" + i);
            excelUserOrdersDto.setEmployePassword("password" + i);
            excelUserOrdersDto.setProductId((long) (i + 1));
            excelUserOrdersDto.setQuantity(12.12 + i);
            list.add(excelUserOrdersDto);
        }

        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("用户订单数据模板", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 定义注意事项内容
            List<String> notices = Arrays.asList(
                    "注意事项：1. 请按照示例格式填写数据；2. 用户名和密码不可为空；3. 产品ID和数量需为数字；4. 订单日期格式为：yyyy年M月d天",
                    "温馨提示：请使用Excel 2007及以上版本打开，数据填写完成后请保存为.xlsx格式"
            );

            // 写入Excel（注册注意事项处理器）
            EasyExcel.write(response.getOutputStream(), ExcelUserOrdersDto.class)
                    .registerWriteHandler(new NoticeRowWriteHandler(notices))
                    .sheet()
                    .doWrite(list);
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }

    }

}
