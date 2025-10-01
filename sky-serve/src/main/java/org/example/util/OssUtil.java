package org.example.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.example.property.OssProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class OssUtil {
    @Autowired
    OssProperty ossProperty;
    public String upload(InputStream inputStream, String originalFilename) {

        String fileName = UUID.randomUUID().toString().replace("-", "") + "_" + originalFilename;
        OSS ossClient = new OSSClientBuilder().build(ossProperty.getEndpoint(), ossProperty.getAccessKeyId(), ossProperty.getAccessKeySecret());
        try {
            ossClient.putObject(ossProperty.getBucketName(), fileName, inputStream);
        }  finally {
            ossClient.shutdown();
        }
        return ossProperty.getUrlPrefix() + "/" + fileName;

    }
    public void delete(String fileUrl) {
        String objectName = fileUrl.replace(ossProperty.getUrlPrefix()  + "/", "");
        OSS ossClient = new OSSClientBuilder().build(ossProperty.getEndpoint(), ossProperty.getAccessKeyId(), ossProperty.getAccessKeySecret());
        try {
            ossClient.deleteObject(ossProperty.getBucketName(), objectName);
        } finally {
            ossClient.shutdown();
        }
    }
}
