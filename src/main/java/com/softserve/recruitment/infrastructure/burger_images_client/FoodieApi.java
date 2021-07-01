package com.softserve.recruitment.infrastructure.burger_images_client;

import com.softserve.recruitment.domain.image_downloader.BurgerImageDownloaderClient;
import com.softserve.recruitment.infrastructure.burger_images_client.endpoint.BurgerRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FoodieApi implements BurgerImageDownloaderClient {

  public static final String BURGER_REQUEST_ENDPOINT = "/images/burger/burger%s.jpg";
  private final String domainName;
  private final BurgerRequest burgerRequest;

  public FoodieApi(FoodieWebClientConfig foodieWebClientConfig) {
    burgerRequest = new BurgerRequest(foodieWebClientConfig.foodieWebClient());
    domainName = foodieWebClientConfig.getUrl();
  }

  @Override
  public Mono<String> getBurgerImage(String uri) {
    return burgerRequest.getBurgerImage(uri);
  }

  @Override
  public Mono<String> getBurgerImageUrl(String id) {
    return Mono.just(String.format(domainName + BURGER_REQUEST_ENDPOINT, id));
  }
}