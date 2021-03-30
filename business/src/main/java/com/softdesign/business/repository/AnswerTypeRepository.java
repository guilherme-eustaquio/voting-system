package com.softdesign.business.repository;

import com.softdesign.business.domain.AnswerType;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AnswerTypeRepository extends ReactiveCrudRepository<AnswerType, String> {
    Mono<AnswerType> findByAnswer(String answer);
}
