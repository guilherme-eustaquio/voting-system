package com.softdesign.business.repository;

import com.softdesign.business.domain.Theme;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ThemeRepository extends ReactiveCrudRepository<Theme, String> {
    Mono<Theme> findByQuestion(String question);
}
