package com.softdesign.votingsystem.application.controller;

import com.softdesign.business.controller.ISessionController;
import com.softdesign.business.data.AssociatedSessionData;
import com.softdesign.business.data.SessionData;
import com.softdesign.business.domain.AssociatedSession;
import com.softdesign.business.domain.Session;
import com.softdesign.business.response.AssociatedSessionResponse;
import com.softdesign.business.response.SessionResponse;
import com.softdesign.business.service.SessionService;
import com.softdesign.votingsystem.application.validation.SessionValidation;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/sessions")
@Api(value = "API de gerenciamento de sessões")
public class SessionController implements ISessionController {

    @Autowired
    SessionService sessionService;

    @Autowired
    SessionValidation sessionValidation;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Mono<SessionResponse> save(SessionData sessionData) {

        Session sessionToSave = modelMapper.map(sessionData, Session.class);

        return sessionValidation.validateCreateSession(sessionToSave)
            .then(sessionService.save(sessionToSave))
            .map(sessionResult -> modelMapper.map(sessionResult, SessionResponse.class));
    }

    @Override
    public Mono<AssociatedSessionResponse> answerSession(AssociatedSessionData associatedSessionData) {

        AssociatedSession associatedSessionToSave = modelMapper.map(associatedSessionData, AssociatedSession.class);

        return sessionValidation.validateCreateAnswer(associatedSessionToSave)
                .then(sessionService.answerSession(associatedSessionToSave))
                .map(associatedSession -> modelMapper.map(associatedSession, AssociatedSessionResponse.class));
    }

    @Override
    public Mono<Void> delete(String id) {
        return sessionValidation.validateDeleteSession(id)
            .then(sessionService.delete(id));
    }

    @Override
    public Flux<SessionResponse> findAll(long page, long size) {
        return sessionService.findAll().map(session -> modelMapper.map(session, SessionResponse.class))
            .skip(page * size).take(size);
    }

    @Override
    public Mono<SessionResponse> findById(String id) {
        return sessionService.findById(id).map(session -> modelMapper.map(session, SessionResponse.class));
    }
}
