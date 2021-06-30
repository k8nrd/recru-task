package com.softserve.recruitment.infrastructure.burger_images_client;

import reactor.core.publisher.Mono;

public interface BurgerImageDownloaderClient {
    Mono<String> getBurgerImage(String id);
    Mono<String> getBurgerImageUrl(String id);
}
