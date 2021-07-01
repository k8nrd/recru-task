package com.softserve.recruitment.infrastructure.burger_images_client.endpoint;

import org.springframework.web.reactive.function.client.WebClientException;

public class BurgerRequestClientException extends WebClientException {

  public BurgerRequestClientException(String msg) {
    super(msg);
  }
}
