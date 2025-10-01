package org.example.service.impl;

import com.alibaba.excel.EasyExcel;
import org.example.config.ExcelDataListener;
import org.example.dto.ExcelUserOrdersDto;
import org.example.service.UploadFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Service
public class UploadFileServiceImpl implements UploadFileService {
    @Override
    public int uploadExcel(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), ExcelUserOrdersDto.class,new ExcelDataListener<ExcelUserOrdersDto>()).sheet().headRowNumber(4).doRead();
//        EasyExcel.read(file.getInputStream(),  ExcelUserOrdersDto.class, new ExcelDataListener<ExcelUserOrdersDto>()).doReadAll();
        return 0;
    }
}
