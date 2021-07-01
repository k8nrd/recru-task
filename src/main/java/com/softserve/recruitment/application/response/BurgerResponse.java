package com.softserve.recruitment.application.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BurgerResponse {
  private String originalUrl;
  private String image;
}
