package com.softdesign.votingsystem.application.validation;

import com.softdesign.business.domain.AnswerType;
import com.softdesign.business.repository.AnswerTypeRepository;
import com.softdesign.business.repository.AssociatedSessionRepository;
import com.softdesign.votingsystem.application.constants.ErrorCode;
import com.softdesign.votingsystem.application.exception.AnswerTypeAlreadyExistsException;
import com.softdesign.votingsystem.application.exception.AnswerTypeNotFoundException;
import com.softdesign.votingsystem.application.exception.ConstraintBreakException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AnswerTypeValidation {

    @Autowired
    private AssociatedSessionRepository associatedSessionRepository;

    @Autowired
    private AnswerTypeRepository answerTypeRepository;

    public Mono<AnswerType> validateCreateAnswer(AnswerType answerType) {
        return checkIfAnswerAlreadyExists(answerType);
    }

    public Mono<String> validateDeleteAnswerType(String answerType) {
        return checkIfAnswerTypeExists(answerType)
               .then(checkIfAnswerTypeIsRestricted(answerType));
    }

    private Mono<AnswerType> checkIfAnswerAlreadyExists(AnswerType answerType) {
        return answerTypeRepository.findByAnswer(answerType.getAnswer()).doOnSuccess((result) -> {
            if(result != null) {
                throw new AnswerTypeAlreadyExistsException(
                    ErrorCode.ANSWER_ALREADY_EXISTS.getMessage(),
                    ErrorCode.ANSWER_ALREADY_EXISTS.getCode()
                );
            }
        });
    }

    private Mono<String> checkIfAnswerTypeExists(String answerType) {
        return answerTypeRepository.findById(answerType).doOnSuccess((result) -> {
            if(result == null) {
                throw new AnswerTypeNotFoundException(
                    ErrorCode.ANSWER_TYPE_NOT_FOUND.getMessage(),
                    ErrorCode.ANSWER_TYPE_NOT_FOUND.getCode()
                );
            }
        }).then(Mono.just(answerType));
    }

    private Mono<String> checkIfAnswerTypeIsRestricted(String answerType) {

        return associatedSessionRepository.findByAnswerType(answerType)
            .next()
            .doOnSuccess((result) -> {
                if(result != null) {
                    throw new ConstraintBreakException(
                        ErrorCode.CONSTRAINT_ERROR.getMessage(),
                        ErrorCode.CONSTRAINT_ERROR.getCode()
                    );
                }
        }).then(Mono.just(answerType));
    }
}
