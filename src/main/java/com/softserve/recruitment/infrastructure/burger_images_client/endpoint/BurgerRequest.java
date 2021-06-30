package com.softserve.recruitment.infrastructure.burger_images_client.endpoint;

import java.util.Base64;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BurgerRequest {

  private final WebClient webClient;

  public BurgerRequest(WebClient webClient) {
    this.webClient = webClient;
  }

  public Mono<String> getBurgerImage(String uri) {
    return webClient
        .get()
        .uri(uri)
        .accept(MediaType.IMAGE_JPEG)
        .retrieve()
        .bodyToMono(byte[].class)
        .map(bytes -> Base64.getEncoder()
                            .encodeToString(bytes));
  }
}
