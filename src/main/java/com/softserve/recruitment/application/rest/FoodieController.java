package com.softserve.recruitment.application.rest;

import com.softserve.recruitment.application.response.BurgerResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FoodieController {

  @GetMapping("/burgers/{id}")
  public Mono<ResponseEntity<BurgerResponse>> getBurger(@PathVariable String id) {
    BurgerResponse burgerResponse = new BurgerResponse();
    burgerResponse.setOriginalUrl(id);
    burgerResponse.setImage("base64");
    return Mono.just(ResponseEntity.ok(burgerResponse));
  }
}
