package org.example.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadFileService {
    int uploadExcel(MultipartFile file) throws IOException;
}
