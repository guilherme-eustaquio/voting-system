package com.softdesign.votingsystem.application.validation;

import com.softdesign.business.domain.AnswerType;
import com.softdesign.business.domain.Associated;
import com.softdesign.business.domain.AssociatedSession;
import com.softdesign.business.domain.Session;
import com.softdesign.business.domain.Theme;
import com.softdesign.business.repository.AnswerTypeRepository;
import com.softdesign.business.repository.AssociatedRepository;
import com.softdesign.business.repository.AssociatedSessionRepository;
import com.softdesign.business.repository.SessionRepository;
import com.softdesign.business.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class SessionValidation {

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AssociatedRepository associatedRepository;

    @Autowired
    private AssociatedSessionRepository associatedSessionRepository;

    @Autowired
    private AnswerTypeRepository answerTypeRepository;

    public Mono<Session> validateCreateSession(Session session) {

        if(session.getTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException();
        }

        Mono<Session> sessionMono = Mono.just(session);

        return checkIfThemeExists(session).then(sessionMono);
    }

    public Mono<AssociatedSession> validateCreateAnswer(AssociatedSession associatedSession) {

        Mono<AssociatedSession> associatedSessionMono = Mono.just(associatedSession);

        return checkIfSessionExistsAndItsTime(associatedSession)
            .zipWith(checkIfAnswerTypeExists(associatedSession))
            .zipWith(checkIfAssociatedExists(associatedSession))
            .zipWith(checkIfAssociatedAndSessionAlreadyAnswered(associatedSession))
            .then(associatedSessionMono);
    }

    private Mono<Theme> checkIfThemeExists(Session session) {
        return themeRepository.findById(session.getTheme()).doOnSuccess((result) -> {
            if(result == null) {
                throw new RuntimeException();
            }
        });
    }

    private Mono<AnswerType> checkIfAnswerTypeExists(AssociatedSession associatedSession) {
        return answerTypeRepository.findById(associatedSession.getAnswerType()).doOnSuccess((result) -> {
            if(result == null) {
                throw new RuntimeException();
            }
        });
    }

    private Mono<Session> checkIfSessionExistsAndItsTime(AssociatedSession associatedSession) {
        return sessionRepository.findById(associatedSession.getSession()).doOnSuccess((result) -> {
            if(result == null || result.getTime().isBefore(LocalDateTime.now())) {
                throw new RuntimeException();
            }
        });
    }

    private Mono<Associated> checkIfAssociatedExists(AssociatedSession associatedSession) {
        return associatedRepository.findById(associatedSession.getAssociated()).doOnSuccess((result) -> {
            if(result == null) {
                throw new RuntimeException();
            }
        });
    }

    private Mono<AssociatedSession> checkIfAssociatedAndSessionAlreadyAnswered(AssociatedSession associatedSession) {
        return associatedSessionRepository.findByAssociatedAndSession(associatedSession.getAssociated(), associatedSession.getSession()).doOnSuccess((result) -> {
            if(result != null) {
                throw new RuntimeException();
            }
        });
    }
}
