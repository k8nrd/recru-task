package com.softserve.recruitment.domain.service;

import com.softserve.recruitment.domain.Burger;
import reactor.core.publisher.Mono;

public interface BurgerService {
  Mono<Burger> getBurger(String id);
}
