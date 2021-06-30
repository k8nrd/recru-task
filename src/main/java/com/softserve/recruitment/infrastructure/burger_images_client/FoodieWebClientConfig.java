package com.softserve.recruitment.infrastructure.burger_images_client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class FoodieWebClientConfig {

  @Value("${foodie.client.url}")
  private String foodieUrl;

  @Bean
  public WebClient foodieWebClient() {
    return WebClient
        .builder()
        .baseUrl(foodieUrl)
        .build();
  }

}
