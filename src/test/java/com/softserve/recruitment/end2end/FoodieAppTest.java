package com.softserve.recruitment.end2end;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class FoodieAppTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void shouldReturnResponseBody() throws InterruptedException {
    webTestClient
        .get()
        .uri("/burgers/{id}", 1)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody();
  }
}
