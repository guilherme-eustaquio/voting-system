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
import com.softdesign.votingsystem.application.constants.ErrorCode;
import com.softdesign.votingsystem.application.exception.AnswerTypeNotFoundException;
import com.softdesign.votingsystem.application.exception.AssociatedNotFoundException;
import com.softdesign.votingsystem.application.exception.ConstraintBreakException;
import com.softdesign.votingsystem.application.exception.SessionAlreadyAnsweredException;
import com.softdesign.votingsystem.application.exception.SessionExpiredException;
import com.softdesign.votingsystem.application.exception.SessionNotFoundException;
import com.softdesign.votingsystem.application.exception.SessionTimeInvalidException;
import com.softdesign.votingsystem.application.exception.ThemeNotFoundException;
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

    public Mono<Theme> validateCreateSession(Session session) {
        return checkIfThemeExists(session);
    }

    public Mono<String> validateDeleteSession(String id) {
        return checkIfSessionIsRestricted(id);
    }

    public Mono<AssociatedSession> validateCreateAnswer(AssociatedSession associatedSession) {

        Mono<AssociatedSession> associatedSessionMono = Mono.just(associatedSession);

        return checkIfSessionExistsAndItsTime(associatedSession)
            .then(checkIfAnswerTypeExists(associatedSession))
            .then(checkIfAssociatedExists(associatedSession))
            .then(checkIfAssociatedAndSessionAlreadyAnswered(associatedSession))
            .then(associatedSessionMono);
    }

    private Mono<Theme> checkIfThemeExists(Session session) {
        return themeRepository.findById(session.getTheme()).doOnSuccess((result) -> {
            if(result == null) {
                throw new ThemeNotFoundException(
                    ErrorCode.THEME_NOT_FOUND.getMessage(),
                    ErrorCode.THEME_NOT_FOUND.getCode()
                );
            }

            if(session.getTime() != null && session.getTime().isBefore(LocalDateTime.now())) {
                throw new SessionTimeInvalidException(
                        ErrorCode.SESSION_TIME_INVALID.getMessage(),
                        ErrorCode.SESSION_TIME_INVALID.getCode()
                );
            }
        });
    }

    private Mono<AnswerType> checkIfAnswerTypeExists(AssociatedSession associatedSession) {
        return answerTypeRepository.findById(associatedSession.getAnswerType()).doOnSuccess((result) -> {
            if(result == null) {
                throw new AnswerTypeNotFoundException(
                    ErrorCode.ANSWER_TYPE_NOT_FOUND.getMessage(),
                    ErrorCode.ANSWER_TYPE_NOT_FOUND.getCode()
                );
            }
        });
    }

    private Mono<Session> checkIfSessionExistsAndItsTime(AssociatedSession associatedSession) {
        return sessionRepository.findById(associatedSession.getSession()).doOnSuccess((result) -> {
            if(result == null) {
                throw new SessionNotFoundException(
                    ErrorCode.SESSION_NOT_FOUND.getMessage(),
                    ErrorCode.SESSION_NOT_FOUND.getCode()
                );
            }

            if(result.getTime() != null && result.getTime().isBefore(LocalDateTime.now())) {
                throw new SessionExpiredException(
                    ErrorCode.SESSION_EXPIRED.getMessage(),
                    ErrorCode.SESSION_EXPIRED.getCode()
                );
            }
        });
    }

    private Mono<Associated> checkIfAssociatedExists(AssociatedSession associatedSession) {
        return associatedRepository.findById(associatedSession.getAssociated()).doOnSuccess((result) -> {
            if(result == null) {
                throw new AssociatedNotFoundException(
                    ErrorCode.ASSOCIATED_NOT_FOUND.getMessage(),
                    ErrorCode.ASSOCIATED_NOT_FOUND.getCode()
                );
            }
        });
    }

    private Mono<AssociatedSession> checkIfAssociatedAndSessionAlreadyAnswered(AssociatedSession associatedSession) {
        return associatedSessionRepository.findByAssociatedAndSession(associatedSession.getAssociated(), associatedSession.getSession()).doOnSuccess((result) -> {
            if(result != null) {
                throw new SessionAlreadyAnsweredException(
                    ErrorCode.SESSION_ALREADY_ANSWERED.getMessage(),
                    ErrorCode.SESSION_ALREADY_ANSWERED.getCode()
                );
            }
        });
    }

    private Mono<String> checkIfSessionIsRestricted(String session) {

        return associatedSessionRepository.findBySession(session)
            .next()
            .doOnSuccess((result) -> {
                if(result != null) {
                    throw new ConstraintBreakException(
                        ErrorCode.CONSTRAINT_ERROR.getMessage(),
                        ErrorCode.CONSTRAINT_ERROR.getCode()
                    );
                }
        }).then(Mono.just(session));
    }
}
