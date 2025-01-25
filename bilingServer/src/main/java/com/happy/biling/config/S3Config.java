package com.happy.biling.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

    @Value("${AWS_IAM_ACCESS_KEY}")
    private String accessKey;

    @Value("${AWS_IAM_SECRET_KEY}")
    private String secretKey;

    @Value("${AWS_REGION_STATIC}")
    private String region;

    @Bean
    @Primary
    public AwsCredentialsProvider awsCredentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
    }

    @Bean
    @Primary
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(awsCredentialsProvider())
                .region(Region.of(region))
                .build();
    }
}
