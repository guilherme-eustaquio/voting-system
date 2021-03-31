package com.softdesign.votingsystem.application.validation;

import com.softdesign.business.domain.Associated;
import com.softdesign.business.repository.AssociatedRepository;
import com.softdesign.business.repository.AssociatedSessionRepository;
import com.softdesign.votingsystem.application.constants.ErrorCode;
import com.softdesign.votingsystem.application.exception.AnswerTypeAlreadyExistsException;
import com.softdesign.votingsystem.application.exception.AssociatedAlreadyExistsException;
import com.softdesign.votingsystem.application.exception.AssociatedNotFoundException;
import com.softdesign.votingsystem.application.exception.ConstraintBreakException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AssociatedValidation {

    @Autowired
    private AssociatedSessionRepository associatedSessionRepository;

    @Autowired
    private AssociatedRepository associatedRepository;

    //@Autowired
    //private WebClient webClient;

    public Mono<Associated> validateCreateAssociated(Associated associated) {
        return checkIfCpfAlreadyExists(associated);
    }

    public Mono<String> validatedDeleteAssociated(String associated) {
        return checkIfAssociatedExists(associated)
               .then(checkIfAssociatedIsRestricted(associated));
    }

    private Mono<Associated> checkIfCpfAlreadyExists(Associated associated) {
        return associatedRepository.findByCpf(associated.getCpf()).doOnSuccess((result) -> {
            if(result != null) {
                throw new AssociatedAlreadyExistsException(
                    ErrorCode.ASSOCIATED_ALREADY_EXISTS.getMessage(),
                    ErrorCode.ASSOCIATED_ALREADY_EXISTS.getCode()
                );
            }
        });
    }

    private Mono<String> checkIfAssociatedExists(String associated) {
        return associatedRepository.findById(associated).doOnSuccess((result) -> {
            if(result == null) {
                throw new AssociatedNotFoundException(
                    ErrorCode.ASSOCIATED_NOT_FOUND.getMessage(),
                    ErrorCode.ASSOCIATED_NOT_FOUND.getCode()
                );
            }
        }).then(Mono.just(associated));
    }

    private Mono<String> checkIfAssociatedIsRestricted(String associated) {
        return associatedSessionRepository.findByAssociated(associated)
            .next()
            .doOnSuccess((result) -> {
                if(result != null) {
                    throw new ConstraintBreakException(
                        ErrorCode.CONSTRAINT_ERROR.getMessage(),
                        ErrorCode.CONSTRAINT_ERROR.getCode()
                    );
                }
        }).then(Mono.just(associated));
    }
}
