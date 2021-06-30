package com.softserve.recruitment.domain.service;

import static org.mockito.BDDMockito.given;

import com.softserve.recruitment.domain.Burger;
import com.softserve.recruitment.infrastructure.burger_images_client.BurgerImageDownloaderClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class BurgerServiceTest {

  @Mock
  private BurgerImageDownloaderClient burgerImageDownloaderClient;

  private BurgerService burgerService;

  @BeforeEach
  void setUp(){
    burgerService = new DomainBurgerService(burgerImageDownloaderClient);
  }

  @Test
  void shouldReturnBurger(){
    //given
    final String id = "1";
    final String url = "url";
    given(burgerImageDownloaderClient.getBurgerImageUrl(id)).willReturn(Mono.just(url));
    given(burgerImageDownloaderClient.getBurgerImage(url)).willReturn(Mono.just("base64"));

    StepVerifier.create(burgerService.getBurger(id))
                .expectNext(Burger.builder()
                                  .originalUrl("url")
                                  .image("base64")
                                  .build())
                .verifyComplete();
  }
}
