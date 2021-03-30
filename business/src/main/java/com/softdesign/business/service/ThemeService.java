package com.softdesign.business.service;

import com.softdesign.business.domain.Theme;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ThemeService {
    Flux<Theme> findAll();
    Mono<Theme> findById(String id);
    Mono<Theme> findByQuestion(String question);
    Mono<Theme> save(Theme theme);
    Mono<Void> delete(String id);
}
