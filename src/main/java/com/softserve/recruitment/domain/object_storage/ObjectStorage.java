package com.softserve.recruitment.domain.object_storage;

import reactor.core.publisher.Mono;

public interface ObjectStorage {

  Mono<Void> saveObject(String objectId, String value);

  Mono<String> getImageString(String objectId);
}
