package com.softdesign.business.service;

import com.softdesign.business.domain.Associated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AssociatedService {
    Flux<Associated> findAll();
    Mono<Associated> findById(String id);
    Mono<Associated> findByCpf(String cpf);
    Mono<Associated> save(Associated associated);
    Mono<Void> delete(String id);
}
