package com.softserve.recruitment.end2end;

import com.softserve.recruitment.testConfiguration.S3TestContainer;
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

/* Assumed that foodie will always work, so I didnt create mock for this */

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "30000")
public class FoodieAppTest extends S3TestContainer {

  private static final String BUCKET_NAME = "bucket1";

  @Autowired
  private WebTestClient webTestClient;

  @Autowired
  S3AsyncClient s3AsyncClientl;

  @BeforeEach
  public void setUp() throws ExecutionException, InterruptedException {
    s3AsyncClientl.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build()).get();
  }

  @AfterEach
  public void tearDown(){
    s3AsyncClientl.deleteBucket(DeleteBucketRequest.builder().bucket(BUCKET_NAME).build());
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
}
