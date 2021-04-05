package com.softdesign.business.service.impl;

import com.softdesign.business.domain.AssociatedSession;
import com.softdesign.business.domain.Session;
import com.softdesign.business.repository.AssociatedSessionRepository;
import com.softdesign.business.repository.SessionRepository;
import com.softdesign.business.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class SessionServiceImpl implements SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AssociatedSessionRepository associatedSessionRepository;

    @Override
    public Flux<Session> findAll() {
        return sessionRepository.findAll();
    }

    @Override
    public Flux<Session> findByTheme(String theme) {
        return sessionRepository.findByTheme(theme);
    }

    @Override
    public Mono<Session> findById(String id) { return sessionRepository.findById(id); }

    @Override
    public Mono<Session> save(Session session) {

        session.setCreatedAt(LocalDateTime.now());

        if(session.getTime() == null) {
            session.setTime(LocalDateTime.now().plusMinutes(1));
        }

        return sessionRepository.save(session);
    }

    @Override
    public Mono<AssociatedSession> answerSession(AssociatedSession associatedSession) {
        associatedSession.setCreatedAt(LocalDateTime.now());
        return associatedSessionRepository.save(associatedSession);
    }

    @Override
    public Mono<Void> delete(String id) {
        return sessionRepository.deleteById(id);
    }
}
