package com.softdesign.business.service;

import com.softdesign.business.domain.AssociatedSession;
import com.softdesign.business.domain.Session;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SessionService {
    Flux<Session> findAll();
    Flux<Session> findByTheme(String theme);
    Mono<Session> findById(String id);
    Mono<Session> save(Session session);
    Mono<AssociatedSession> answerSession(AssociatedSession associatedSession);
    Mono<Void> delete(String id);
}
