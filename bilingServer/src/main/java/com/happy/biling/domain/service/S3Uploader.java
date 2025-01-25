package com.happy.biling.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class S3Uploader {

    private final S3Client s3Client;

    @Value("${AWS_S3_BUCKET_NAME}")
    private String bucketName;

    public S3Uploader(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file, String folderName) {
        log.info("Uploading file to S3. Folder: {}, File Name: {}", folderName, file.getOriginalFilename());

        if (folderName == null || folderName.isEmpty()) {
            throw new IllegalArgumentException("업로드 폴더 이름이 지정되지 않았습니다.");
        }

        String fileName = folderName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            PutObjectResponse response = s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            log.info("S3 PutObject Response Status: {}", response.sdkHttpResponse().statusCode());

            if (response.sdkHttpResponse().isSuccessful()) {
                String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
                log.info("File successfully uploaded to S3: {}", fileUrl);
                return fileUrl;
            } else {
                throw new RuntimeException("S3 업로드 실패");
            }
        } catch (IOException e) {
            log.error("파일 업로드 중 오류 발생", e);
            throw new RuntimeException("파일 업로드 중 오류 발생", e);
        }
    }


    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }

        // 파일 키 추출
        String fileKey = fileUrl.substring(fileUrl.indexOf(".s3.amazonaws.com/") + 17);

        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build());
    }

}

