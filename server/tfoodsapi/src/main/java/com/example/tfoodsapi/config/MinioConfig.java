package com.example.tfoodsapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;

@Configuration
public class MinioConfig {
    @Value("${spring.data.minio.url}")
    private String minioUrl;

    @Value("${spring.data.minio.access-key}")
    private String accessKey;

    @Value("${spring.data.minio.secret-key}")
    private String secretKey;

    @Value("${spring.data.minio.bucket-name}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        try {
            return MinioClient.builder()
                    .endpoint(minioUrl) // Địa chỉ của MinIO
                    .credentials(accessKey, secretKey) // Thông tin xác thực
                    .build();

        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
}
