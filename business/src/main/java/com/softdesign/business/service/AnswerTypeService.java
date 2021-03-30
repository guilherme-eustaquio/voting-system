package com.softdesign.business.service;

import com.softdesign.business.domain.AnswerType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AnswerTypeService {
    Flux<AnswerType> findAll();
    Mono<AnswerType> findById(String id);
    Mono<AnswerType> findByAnswer(String answer);
    Mono<AnswerType> save(AnswerType answerType);
    Mono<Void> delete(String id);
}
