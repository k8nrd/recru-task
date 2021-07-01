package com.softserve.recruitment.infrastructure.s3_client;

import com.softserve.recruitment.domain.object_storage.ObjectStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.BytesWrapper;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
public class AmazonS3Api implements ObjectStorage {

  private static final Logger logger = LoggerFactory.getLogger(AmazonS3Api.class);
  private final S3AsyncClient s3Client;
  @Value("${s3.bucket}")
  private String bucket;

  public AmazonS3Api(S3AsyncClient s3Client) {
    this.s3Client = s3Client;
  }

  @Override
  public Mono<Void> saveObject(String objectId, String value) {
    return Mono.fromFuture(s3Client.putObject(PutObjectRequest.builder()
                                                              .key(String.valueOf(objectId.hashCode()))
                                                              .bucket(bucket)
                                                              .build(), AsyncRequestBody.fromString(value)))
               .doOnNext(putObjectResponse -> logger.info(String.format("Object id=%s has been saved", objectId)))
               .onErrorResume(throwable -> {
                 logger.error(String.format("Object Id=%s cannot be saved,  Cause=%s", objectId, throwable.getCause()));
                 return Mono.empty();
               })
               .then();

  }

  @Override
  public Mono<String> getImageString(String objectId) {
    return Mono.fromFuture(s3Client.getObject(GetObjectRequest.builder()
                                                              .key(String.valueOf(objectId.hashCode()))
                                                              .bucket(bucket)
                                                              .build(), AsyncResponseTransformer.toBytes()))
               .map(BytesWrapper::asUtf8String)
               .filter(image -> !image.isBlank())
               .doOnNext(getObjectResponse -> logger.info(String.format("Object value id=%s has been fetched", objectId)))
               .onErrorResume(throwable -> {
                 logger.error(String.format("Object Id=%s doesn't exist or problem with S3,  Cause=%s", objectId,
                     throwable.getMessage()));
                 return Mono.empty();
               });
  }
}
