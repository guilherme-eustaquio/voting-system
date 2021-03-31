package com.softdesign.votingsystem.application.validation;

import com.softdesign.business.repository.SessionRepository;
import com.softdesign.votingsystem.application.constants.ErrorCode;
import com.softdesign.votingsystem.application.exception.ConstraintBreakException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ThemeValidation {

    @Autowired
    private SessionRepository sessionRepository;

    public Mono<String> validateDeleteTheme(String theme) {
        return checkIfThemeIsRestricted(theme);
    }

    private Mono<String> checkIfThemeIsRestricted(String theme) {

        return sessionRepository.findByTheme(theme)
            .next()
            .doOnSuccess((result) -> {
                if(result != null) {
                    throw new ConstraintBreakException(
                        ErrorCode.CONSTRAINT_ERROR.getMessage(),
                        ErrorCode.CONSTRAINT_ERROR.getCode()
                    );
                }
        }).then(Mono.just(theme));
    }
}
