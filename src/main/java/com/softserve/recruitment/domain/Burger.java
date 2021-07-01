package com.softserve.recruitment.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class Burger {
  private String originalUrl;
  private String image;
}
