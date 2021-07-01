package com.softserve.recruitment.domain.service;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.softserve.recruitment.domain.Burger;
import com.softserve.recruitment.domain.image_downloader.BurgerImageDownloaderClient;
import com.softserve.recruitment.domain.object_storage.ObjectStorage;
import com.softserve.recruitment.infrastructure.burger_images_client.endpoint.BurgerRequestServerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class BurgerServiceTest {

  @Mock
  private BurgerImageDownloaderClient burgerImageDownloaderClient;
  @Mock
  private ObjectStorage objectStorage;

  @InjectMocks
  private DomainBurgerService burgerService;


  @Test
  void shouldReturnCachedBurgerWhenBurgerInStorage() {
    final String id = "1";
    final String url = "url";
    when(burgerImageDownloaderClient.getBurgerImageUrl(id)).thenReturn(Mono.just(url));
    when(objectStorage.getImageString(url)).thenReturn(Mono.just("base64"));

    StepVerifier.create(burgerService.getBurger(id))
                .expectNext(Burger.builder()
                                  .originalUrl("url")
                                  .image("base64")
                                  .build())
                .verifyComplete();
  }

  @Test
  void shouldFetchReturnAndSaveInCacheBurgerWhenBurgerIsNotInStorage() {
    final String id = "1";
    final String url = "url";
    final String value = "base64";

    when(burgerImageDownloaderClient.getBurgerImageUrl(id)).thenReturn(Mono.just(url));
    when(objectStorage.getImageString(url)).thenReturn(Mono.empty());
    when(burgerImageDownloaderClient.getBurgerImage(url)).thenReturn(Mono.just(value));
    when(objectStorage.saveObject(url, value)).thenReturn(Mono.empty());

    StepVerifier.create(burgerService.getBurger(id))
                .expectNext(Burger.builder()
                                  .originalUrl("url")
                                  .image("base64")
                                  .build())
                .verifyComplete();

    verify(objectStorage).saveObject(url,value);
  }

  @Test
  void shouldNotSaveInCacheBurgerWhenBurgerReuqestThrowException() {
    final String id = "1";
    final String url = "url";
    final String value = "base64";

    when(burgerImageDownloaderClient.getBurgerImageUrl(id)).thenReturn(Mono.just(url));
    when(objectStorage.getImageString(url)).thenReturn(Mono.empty());
    when(burgerImageDownloaderClient.getBurgerImage(url)).thenReturn(Mono.error(new BurgerRequestServerException("Problem")));

    StepVerifier.create(burgerService.getBurger(id))
                .expectError(BurgerRequestServerException.class)
                .verify();

    verify(objectStorage, never()).saveObject(url,value);
  }
}
