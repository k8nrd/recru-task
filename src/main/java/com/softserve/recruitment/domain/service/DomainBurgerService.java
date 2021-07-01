package com.softserve.recruitment.domain.service;

import com.softserve.recruitment.domain.Burger;
import com.softserve.recruitment.domain.image_downloader.BurgerImageDownloaderClient;
import com.softserve.recruitment.domain.object_storage.ObjectStorage;
import java.util.function.Consumer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DomainBurgerService implements BurgerService {

  private final BurgerImageDownloaderClient burgerImageDownloaderClient;
  private final ObjectStorage objectStorage;

  public DomainBurgerService(BurgerImageDownloaderClient burgerImageDownloaderClient, ObjectStorage objectStorage) {
    this.burgerImageDownloaderClient = burgerImageDownloaderClient;
    this.objectStorage = objectStorage;
  }

  @Override
  public Mono<Burger> getBurger(String id) {
    return burgerImageDownloaderClient.getBurgerImageUrl(id)
        .flatMap(url -> objectStorage.getImageString(url)
                                      .map(image -> createBurger(url, image))
                                      .switchIfEmpty(Mono.defer(() ->fetchImage(url))));
  }

  private Mono<Burger> fetchImage(String url){
    return burgerImageDownloaderClient.getBurgerImage(url)
                                      .map(image -> createBurger(url, image))
                                      .doOnSuccess(cacheImage());
  }

  private Consumer<Burger> cacheImage() {
    return burger -> objectStorage.saveObject(burger.getOriginalUrl(), burger.getImage())
                                  .subscribe();
  }

  private Burger createBurger(String url, String image){
    return Burger.builder()
                 .originalUrl(url)
                 .image(image)
                 .build();
  }

}
