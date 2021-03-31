package com.softdesign.votingsystem.application.validation;

import com.softdesign.business.domain.Associated;
import com.softdesign.business.repository.AssociatedRepository;
import com.softdesign.business.repository.AssociatedSessionRepository;
import com.softdesign.votingsystem.application.constants.ErrorCode;
import com.softdesign.votingsystem.application.exception.AssociatedAlreadyExistsException;
import com.softdesign.votingsystem.application.exception.AssociatedNotFoundException;
import com.softdesign.votingsystem.application.exception.ConstraintBreakException;
import com.softdesign.votingsystem.application.exception.InvalidCpfException;
import com.softdesign.votingsystem.application.response.ApiCpfResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AssociatedValidation {

    @Autowired
    private AssociatedSessionRepository associatedSessionRepository;

    @Autowired
    private AssociatedRepository associatedRepository;

    private final String apiCpf = "https://youhome.herokuapp.com/users";

    private final String expectedResponseCpf = "ABLE_TO_VOTE";

    public Mono<Associated> validateCreateAssociated(Associated associated) {
        return checkIfCpfAlreadyExists(associated)
                .then(checkIfCpfIsValid(associated))
                .then(Mono.just(associated));
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

    private Mono<ApiCpfResponse> checkIfCpfIsValid(Associated associated) {

        String url = String.format("%s/%s", apiCpf, associated.getCpf());

        return WebClient.create()
            .get()
            .uri(url)
            .retrieve().bodyToMono(ApiCpfResponse.class).doOnSuccess((result) -> {
                if(!(expectedResponseCpf.equals(result.getStatus()))) {
                    throw new InvalidCpfException(
                        ErrorCode.INVALID_CPF.getMessage(),
                        ErrorCode.INVALID_CPF.getCode()
                    );
                }
        });
    }
}
