package com.softserve.recruitment.end2end;

import com.softserve.recruitment.testConfiguration.S3TestContainer;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "30000")
public class FoodieAppTest extends S3TestContainer {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void shouldReturnResponseBody() throws InterruptedException, ExecutionException {

    webTestClient
        .get()
        .uri("/burgers/{id}", 1)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody();
  }
}
