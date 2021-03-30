package com.softdesign.business.repository;

import com.softdesign.business.domain.AssociatedSession;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AssociatedSessionRepository extends ReactiveCrudRepository<AssociatedSession, String> {
    Mono<AssociatedSession> findByAssociatedAndSession(String associated, String session);
}
