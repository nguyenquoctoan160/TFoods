package com.example.tfoodsapi.projectpackage.service;

import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.tfoodsapi.projectpackage.model.Callback;

import io.minio.GetObjectArgs;
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

    public Callback<String> uploadFile(MultipartFile file, String Filename) {

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(Filename)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
            return new Callback<String>(null, minioUrl + bucketName + "/" + Filename);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new Callback<String>(e, null);
        }
    }

    public byte[] getFile(String fileName) throws Exception {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .build())) {
            return stream.readAllBytes();
        }
    }
}
