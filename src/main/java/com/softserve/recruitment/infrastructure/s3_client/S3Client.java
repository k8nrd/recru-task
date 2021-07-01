package com.softserve.recruitment.infrastructure.s3_client;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
public class S3Client {

  @Value("${s3.url}")
  private String s3Url;

  @Value("${s3.region}")
  private String s3Region;

  @Value("${s3.access-key}")
  private String s3AccessKey;

  @Value("${s3.secret-key}")
  private String s3SecretKey;

  @Bean
  public S3AsyncClient s3client() {
    return S3AsyncClient.builder()
                        .region(Region.of(s3Region))
                        .endpointOverride(URI.create(s3Url))
                        .credentialsProvider(() -> AwsBasicCredentials.create(s3AccessKey, s3SecretKey))
                        .build();
  }
}
