package com.softserve.recruitment.infrastructure.burger_images_client.endpoint;

import org.springframework.web.reactive.function.client.WebClientException;

public class BurgerRequestException extends WebClientException {

  public BurgerRequestException(String msg) {
    super(msg);
  }
}
