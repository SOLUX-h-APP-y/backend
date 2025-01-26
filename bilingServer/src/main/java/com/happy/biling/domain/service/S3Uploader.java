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
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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

    //    public String uploadFile(MultipartFile file, String folderName) {
//        log.info("Uploading file to S3. Folder: {}, File Name: {}", folderName, file.getOriginalFilename());
//
//        if (folderName == null || folderName.isEmpty()) {
//            throw new IllegalArgumentException("업로드 폴더 이름이 지정되지 않았습니다.");
//        }
//
//        String fileName = folderName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
//
//        try {
//            PutObjectResponse response = s3Client.putObject(
//                    PutObjectRequest.builder()
//                            .bucket(bucketName)
//                            .key(fileName)
//                            .contentType(file.getContentType())
//                            .build(),
//                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
//            );
//
//            log.info("S3 PutObject Response Status: {}", response.sdkHttpResponse().statusCode());
//
//            if (response.sdkHttpResponse().isSuccessful()) {
//                String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
//                log.info("File successfully uploaded to S3: {}", fileUrl);
//                return fileUrl;
//            } else {
//                throw new RuntimeException("S3 업로드 실패");
//            }
//        } catch (IOException e) {
//            log.error("파일 업로드 중 오류 발생", e);
//            throw new RuntimeException("파일 업로드 중 오류 발생", e);
//        }
//    }
    public String uploadFile(MultipartFile file, String folderName) {
        log.info("Uploading file to S3. Folder: {}, Original File Name: {}", folderName, file.getOriginalFilename());

        if (folderName == null || folderName.isEmpty()) {
            throw new IllegalArgumentException("업로드 폴더 이름이 지정되지 않았습니다.");
        }

        // 파일 이름 처리
        String processedFileName = processFileName(file.getOriginalFilename());
        String fileName = folderName + "/" + processedFileName;

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
                // URL 인코딩 처리
                String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replace("+", "%20");
                String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + encodedFileName;
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


    //    public void deleteFile(String fileUrl) {
//        if (fileUrl == null || fileUrl.isEmpty()) {
//            return;
//        }
//
//        // 파일 키 추출
//        String fileKey = fileUrl.substring(fileUrl.indexOf(".s3.amazonaws.com/") + 17);
//
//        s3Client.deleteObject(DeleteObjectRequest.builder()
//                .bucket(bucketName)
//                .key(fileKey)
//                .build());
//    }
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            log.warn("File URL is empty. Nothing to delete.");
            return;
        }

        try {
            // 파일 키 추출 (S3 URL에서 버킷 이름 뒤의 키 부분)
            String encodedFileKey = fileUrl.substring(fileUrl.indexOf(".s3.amazonaws.com/") + 17);

            // URL 디코딩 처리 (띄어쓰기와 특수문자 처리)
            String fileKey = URLDecoder.decode(encodedFileKey, StandardCharsets.UTF_8.toString());

            log.info("Deleting file from S3. File Key: {}", fileKey);

            // S3에서 파일 삭제 요청
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileKey)
                    .build());

            log.info("File successfully deleted from S3: {}", fileUrl);
        } catch (Exception e) {
            log.error("Error while deleting file from S3. File URL: {}", fileUrl, e);
            throw new RuntimeException("파일 삭제 중 오류 발생", e);
        }
    }
    public List<String> uploadMultipleFiles(List<MultipartFile> files, String folderName) {
        log.info("Start uploading multiple files. Folder: {}, Number of Files: {}", folderName, files.size());

        if (files == null || files.isEmpty()) {
            log.error("File list is null or empty.");
            throw new IllegalArgumentException("파일 리스트가 비어 있습니다.");
        }

        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            log.info("Uploading file: {}", file.getOriginalFilename());
            String fileUrl = uploadFile(file, folderName); // 기존 메서드 재사용
            fileUrls.add(fileUrl);
        }

        log.info("Successfully uploaded all files. Uploaded URLs: {}", fileUrls);
        return fileUrls;
    }



    private String processFileName(String originalFileName) {
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("파일 이름이 비어 있습니다.");
        }

        // 확장자 추출
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String baseName = originalFileName.substring(0, originalFileName.lastIndexOf("."));

        // 이름 길이 제한 (최대 20자)
        int maxBaseNameLength = 20;
        if (baseName.length() > maxBaseNameLength) {
            baseName = baseName.substring(0, maxBaseNameLength);
        }

        // 고유한 이름 생성 (UUID 추가)
        String uniqueName = UUID.randomUUID().toString();
        return baseName + "_" + uniqueName + extension;
    }

}

