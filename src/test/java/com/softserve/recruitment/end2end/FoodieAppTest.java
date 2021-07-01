package com.softserve.recruitment.end2end;

import static org.assertj.core.api.Assertions.assertThat;

import com.softserve.recruitment.testConfiguration.S3TestContainer;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.S3Object;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "30000")
public class FoodieAppTest extends S3TestContainer {

  private static final String BUCKET_NAME = "bucket1";
  @Autowired
  S3AsyncClient s3AsyncClientl;
  @Autowired
  private WebTestClient webTestClient;

  @BeforeEach
  public void setUp() throws ExecutionException, InterruptedException {
    s3AsyncClientl.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build()).get();
  }

  @AfterEach
  public void tearDown() throws ExecutionException, InterruptedException {
    deleteAllObjectsFromBucket();
    s3AsyncClientl.deleteBucket(DeleteBucketRequest.builder().bucket(BUCKET_NAME).build()).get();
  }

  @Test
  void shouldReturnResponseBody() {
    webTestClient
        .get()
        .uri("/burgers/{id}", 1)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody();
  }

  @Test
  void whenRequestBurgerThenBurgerShouldBeStoredInS3Cache() throws ExecutionException, InterruptedException {
    webTestClient
        .get()
        .uri("/burgers/{id}", 1)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody();

    assertThat(isBucketEmpty()).isFalse();
  }

  @Test
  void shouldReturnNotFoundWhenIdDoesNotExist() {
    webTestClient
        .get()
        .uri("/burgers/{id}", Long.MAX_VALUE)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody();
  }

  private boolean isBucketEmpty() throws ExecutionException, InterruptedException {
    return getS3Objects().isEmpty();
  }

  private List<S3Object> getS3Objects() throws ExecutionException, InterruptedException {
    return s3AsyncClientl.listObjectsV2(ListObjectsV2Request.builder()
                                                            .bucket(BUCKET_NAME)
                                                            .build()).get().contents();
  }

  private void deleteAllObjectsFromBucket() throws ExecutionException, InterruptedException {
    getS3Objects().forEach(s3Object -> {
      try {
        s3AsyncClientl.deleteObject(DeleteObjectRequest.builder()
                                                       .bucket(BUCKET_NAME)
                                                       .key(s3Object.key())
                                                       .build()).get();
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    });
  }
}
