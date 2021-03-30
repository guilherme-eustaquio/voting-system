package com.softdesign.business.service.impl;

import com.softdesign.business.domain.AnswerType;
import com.softdesign.business.repository.AnswerTypeRepository;
import com.softdesign.business.service.AnswerTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class AnswerTypeServiceImpl implements AnswerTypeService {

    @Autowired
    private AnswerTypeRepository answerTypeRepository;

    @Override
    public Flux<AnswerType> findAll() {
        return answerTypeRepository.findAll();
    }

    @Override
    public Mono<AnswerType> findById(String id) { return answerTypeRepository.findById(id); }

    @Override
    public Mono<AnswerType> findByAnswer(String answer) { return answerTypeRepository.findByAnswer(answer); }

    @Override
    public Mono<AnswerType> save(AnswerType answerType) {
        answerType.setCreatedAt(LocalDateTime.now());
        return answerTypeRepository.save(answerType);
    }

    @Override
    public Mono<Void> delete(String id) {
        return answerTypeRepository.deleteById(id);
    }
}
