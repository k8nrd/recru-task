package com.softserve.recruitment.application.rest;

import com.softserve.recruitment.application.response.BurgerResponse;
import com.softserve.recruitment.domain.service.BurgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FoodieController {

  private final BurgerService burgerService;

  public FoodieController(BurgerService burgerService) {
    this.burgerService = burgerService;
  }

  @GetMapping("/burgers/{id}")
  public Mono<ResponseEntity<BurgerResponse>> getBurger(@PathVariable String id) {
    return burgerService.getBurger(id)
        .map(burger -> ResponseEntity.ok(BurgerResponse.builder()
                                                      .originalUrl(burger.getOriginalUrl())
                                                      .image(burger.getImage())
                                                      .build()));
  }
}
