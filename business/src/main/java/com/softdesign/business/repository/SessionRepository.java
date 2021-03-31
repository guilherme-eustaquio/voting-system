package com.softdesign.business.repository;

import com.softdesign.business.domain.Session;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SessionRepository extends ReactiveCrudRepository<Session, String> {
    Flux<Session> findByTheme(String theme);

}
