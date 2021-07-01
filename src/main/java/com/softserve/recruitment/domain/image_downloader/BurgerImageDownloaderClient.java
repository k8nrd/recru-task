package com.softserve.recruitment.domain.image_downloader;

import reactor.core.publisher.Mono;

public interface BurgerImageDownloaderClient {
    Mono<String> getBurgerImage(String id);
    Mono<String> getBurgerImageUrl(String id);
}
