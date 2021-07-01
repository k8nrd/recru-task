package com.softserve.recruitment.infrastructure.burger_images_client.endpoint;

import java.util.Base64;
import org.springframework.http.HttpStatus;
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
        .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new BurgerRequestException("Not found or incorrect endpoint")))
        .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new BurgerRequestException("Problem with third party service")))
        .bodyToMono(byte[].class)
        .map(bytes -> Base64.getEncoder()
                            .encodeToString(bytes));
  }
}
