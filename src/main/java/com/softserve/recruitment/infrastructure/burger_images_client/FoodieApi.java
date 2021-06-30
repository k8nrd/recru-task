package com.softserve.recruitment.infrastructure.burger_images_client;

import com.softserve.recruitment.infrastructure.burger_images_client.endpoint.BurgerRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class FoodieApi implements BurgerImageDownloaderClient {
  public static final String BURGER_REQUEST_ENDPOINT = "/images/burger/burger%s.jpg";

  private BurgerRequest burgerRequest;

  public FoodieApi(WebClient foodieWebClient){
    burgerRequest = new BurgerRequest(foodieWebClient);
  }

  @Override
  public Mono<String> getBurgerImage(String uri) {
    return burgerRequest.getBurgerImage(uri);
  }

  @Override
  public Mono<String> getBurgerImageUrl(String id) {
    return Mono.just(String.format(BURGER_REQUEST_ENDPOINT, id));
  }
}