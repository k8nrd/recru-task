package com.softserve.recruitment.infrastructure.burger_images_client.endpoint;

import org.springframework.web.reactive.function.client.WebClientException;

public class BurgerRequestServerException extends WebClientException
{

  public BurgerRequestServerException(String msg) {
    super(msg);
  }
}
