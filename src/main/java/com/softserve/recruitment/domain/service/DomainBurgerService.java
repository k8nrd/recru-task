package com.softserve.recruitment.domain.service;

import com.softserve.recruitment.domain.Burger;
import com.softserve.recruitment.infrastructure.burger_images_client.BurgerImageDownloaderClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DomainBurgerService implements BurgerService {

  private final BurgerImageDownloaderClient burgerImageDownloaderClient;

  public DomainBurgerService(BurgerImageDownloaderClient burgerImageDownloaderClient) {
    this.burgerImageDownloaderClient = burgerImageDownloaderClient;
  }

  @Override
  public Mono<Burger> getBurger(String id) {
    return burgerImageDownloaderClient.getBurgerImageUrl(id)
        .flatMap(url -> burgerImageDownloaderClient.getBurgerImage(url)
                                                    .map(image -> Burger.builder()
                                                                        .originalUrl(url)
                                                                        .image(image)
                                                                        .build()));
  }
}
