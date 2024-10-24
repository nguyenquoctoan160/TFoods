package com.example.tfoodsapi.projectpackage.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${spring.data.minio.bucket-name}")
    private String bucketName;

    @Value("${spring.data.minio.url}")
    private String minioUrl;

    public String uploadFile(MultipartFile file) throws Exception {

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(file.getOriginalFilename())
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        }

        return minioUrl + "/" + bucketName + "/" + file.getOriginalFilename();
    }
}
