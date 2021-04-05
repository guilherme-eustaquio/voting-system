package com.softdesign.votingsystem.application.controller;

import com.softdesign.business.data.AssociatedSessionData;
import com.softdesign.business.data.SessionData;
import com.softdesign.business.domain.AssociatedSession;
import com.softdesign.business.domain.Session;
import com.softdesign.business.repository.AssociatedSessionRepository;
import com.softdesign.business.response.AssociatedSessionResponse;
import com.softdesign.business.response.SessionResponse;
import com.softdesign.business.service.SessionService;
import com.softdesign.votingsystem.application.constants.ErrorCode;
import com.softdesign.votingsystem.application.exception.AnswerTypeNotFoundException;
import com.softdesign.votingsystem.application.exception.AssociatedNotFoundException;
import com.softdesign.votingsystem.application.exception.ConstraintBreakException;
import com.softdesign.votingsystem.application.exception.SessionAlreadyAnsweredException;
import com.softdesign.votingsystem.application.exception.SessionExpiredException;
import com.softdesign.votingsystem.application.exception.SessionNotFoundException;
import com.softdesign.votingsystem.application.exception.ThemeNotFoundException;
import com.softdesign.votingsystem.application.scheduler.SessionScheduler;
import com.softdesign.votingsystem.application.util.SessionCreator;
import com.softdesign.votingsystem.application.validation.SessionValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class SessionControllerTest {

    @InjectMocks
    private SessionController sessionController;

    @Mock
    private SessionService sessionService;

    @Mock
    private AssociatedSessionRepository associatedSessionRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SessionValidation sessionValidation;

    @Mock
    private SessionScheduler sessionScheduler;

    private final Session session = SessionCreator.createDataSession();

    private final SessionResponse sessionResponse = SessionCreator.createDataSessionResponse();

    private final SessionData sessionData = SessionCreator.createDataSessionData();

    private final AssociatedSession associatedSession = SessionCreator.createDataAssociatedSession();

    private final AssociatedSessionResponse associatedSessionResponse = SessionCreator.createDataAssociatedSessionResponse();

    private final AssociatedSessionData associatedSessionData = SessionCreator.createDataAssociatedSessionData();

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    @BeforeEach
    public void setup() {

        BDDMockito.when(modelMapper.map(session, SessionResponse.class))
            .thenReturn(sessionResponse);

        BDDMockito.when(modelMapper.map(sessionData, Session.class))
            .thenReturn(session);

        BDDMockito.when(modelMapper.map(associatedSession, AssociatedSessionResponse.class))
            .thenReturn(associatedSessionResponse);

        BDDMockito.when(modelMapper.map(associatedSessionData, AssociatedSession.class))
            .thenReturn(associatedSession);
    }

    @Test
    public void checkIfBlockHoundIsEnabled() {

        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });

            Schedulers.parallel().schedule(task);
            task.get(10l, TimeUnit.SECONDS);
            Assertions.fail("Deve falhar");
        } catch(Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }

    }

    @Test
    @DisplayName("findAll retorna um flux de sessões")
    public void findAll_ReturnFluxOfAssociatedWhenSuccessful() {

        BDDMockito.when(sessionService.findAll())
                .thenReturn(Flux.just(session));

        StepVerifier.create(sessionController.findAll(0, 10))
                .expectSubscription()
                .expectNext(sessionResponse)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de sessão quando ele existe")
    public void findById_ReturnMonoOfSessionWhenSuccessful() {

        BDDMockito.when(sessionService.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(session));

        StepVerifier.create(sessionController.findById("606a4c8b75adf05a9009ae37"))
                .expectSubscription()
                .expectNext(sessionResponse)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de sessão quando ele não existe")
    public void findById_ReturnMonoOfSessionWhenMonoEmptyIsReturned() {

        BDDMockito.when(sessionService.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(sessionController.findById("1"))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }


    @Test
    @DisplayName("save retorna um mono ao salvá-lo")
    public void save_CreatesMonoOfSessionWhenSuccessful() {

        BDDMockito.when(sessionService.save(session))
                .thenReturn(Mono.just(session));

        BDDMockito.when(sessionValidation.validateCreateSession(session))
                .thenReturn(Mono.just(session));

        BDDMockito.doAnswer(i -> null)
                .when(sessionScheduler)
                .scheduleSessionToPublish(session);

        StepVerifier.create(sessionController.save(sessionData))
                .expectSubscription()
                .expectNext(sessionResponse)
                .verifyComplete();
    }

    @Test
    @DisplayName("save gera uma excessão em caso do tema não existir")
    public void save_CreatesMonoOfSessionWhenThemeDoesNotExists() {

        BDDMockito.when(sessionService.save(session))
                .thenReturn(Mono.just(session));

        BDDMockito.when(sessionValidation.validateCreateSession(session))
                .thenThrow(new ThemeNotFoundException(
                        ErrorCode.THEME_NOT_FOUND.getMessage(),
                        ErrorCode.THEME_NOT_FOUND.getCode()
                ));

        BDDMockito.doAnswer(i -> null)
                .when(sessionScheduler)
                .scheduleSessionToPublish(session);


        assertThrows(ThemeNotFoundException.class, () -> {
            sessionController.save(sessionData);
        });
    }


    @Test
    @DisplayName("save gera uma excessão em caso da sessão não existir")
    public void save_CreatesMonoOfAssociatedSessionWhenSessionNotFound() {

        BDDMockito.when(sessionService.answerSession(associatedSession))
            .thenReturn(Mono.just(associatedSession));

        BDDMockito.when(sessionValidation.validateCreateAnswer(associatedSession))
            .thenThrow(new SessionNotFoundException(
                    ErrorCode.SESSION_NOT_FOUND.getMessage(),
                    ErrorCode.SESSION_NOT_FOUND.getCode()
            ));

        BDDMockito.doAnswer(i -> null)
                .when(sessionScheduler)
                .scheduleSessionToPublish(session);

        assertThrows(SessionNotFoundException.class, () -> {
            sessionController.answerSession(associatedSessionData);
        });
    }

    @Test
    @DisplayName("save gera uma excessão em caso da sessão estar expirada")
    public void save_CreatesMonoOfAssociatedSessionWhenSessionExpired() {

        BDDMockito.when(sessionService.answerSession(associatedSession))
                .thenReturn(Mono.just(associatedSession));

        BDDMockito.when(sessionValidation.validateCreateAnswer(associatedSession))
                .thenThrow(new SessionExpiredException(
                    ErrorCode.SESSION_EXPIRED.getMessage(),
                    ErrorCode.SESSION_EXPIRED.getCode()
                ));

        BDDMockito.doAnswer(i -> null)
                .when(sessionScheduler)
                .scheduleSessionToPublish(session);

        assertThrows(SessionExpiredException.class, () -> {
            sessionController.answerSession(associatedSessionData);
        });
    }

    @Test
    @DisplayName("save gera uma excessão em caso do tipo de resposta não existir")
    public void save_CreatesMonoOfAssociatedSessionWhenAnswerTypeNotFound() {

        BDDMockito.when(sessionService.answerSession(associatedSession))
                .thenReturn(Mono.just(associatedSession));

        BDDMockito.when(sessionValidation.validateCreateAnswer(associatedSession))
                .thenThrow(new AnswerTypeNotFoundException(
                        ErrorCode.ANSWER_TYPE_NOT_FOUND.getMessage(),
                        ErrorCode.ANSWER_TYPE_NOT_FOUND.getCode()
                ));

        BDDMockito.doAnswer(i -> null)
                .when(sessionScheduler)
                .scheduleSessionToPublish(session);

        assertThrows(AnswerTypeNotFoundException.class, () -> {
            sessionController.answerSession(associatedSessionData);
        });
    }

    @Test
    @DisplayName("save gera uma excessão em caso do associado não existir")
    public void save_CreatesMonoOfAssociatedSessionWhenAssociatedNotFound() {

        BDDMockito.when(sessionService.answerSession(associatedSession))
            .thenReturn(Mono.just(associatedSession));

        BDDMockito.when(sessionValidation.validateCreateAnswer(associatedSession))
            .thenThrow(new AssociatedNotFoundException(
                    ErrorCode.ASSOCIATED_NOT_FOUND.getMessage(),
                    ErrorCode.ASSOCIATED_NOT_FOUND.getCode()
            ));

        BDDMockito.doAnswer(i -> null)
                .when(sessionScheduler)
                .scheduleSessionToPublish(session);

        assertThrows(AssociatedNotFoundException.class, () -> {
            sessionController.answerSession(associatedSessionData);
        });
    }

    @Test
    @DisplayName("save gera uma excessão em caso do associado já ter respondido dada sessão")
    public void save_CreatesMonoOfAssociatedSessionWhenAssociatedAndSessionAlreadyAnswered() {

        BDDMockito.when(sessionService.answerSession(associatedSession))
            .thenReturn(Mono.just(associatedSession));

        BDDMockito.when(sessionValidation.validateCreateAnswer(associatedSession))
            .thenThrow(new SessionAlreadyAnsweredException(
                    ErrorCode.SESSION_ALREADY_ANSWERED.getMessage(),
                    ErrorCode.SESSION_ALREADY_ANSWERED.getCode()
            ));

        BDDMockito.doAnswer(i -> null)
            .when(sessionScheduler)
            .scheduleSessionToPublish(session);

        assertThrows(SessionAlreadyAnsweredException.class, () -> {
            sessionController.answerSession(associatedSessionData);
        });
    }

    @Test
    @DisplayName("answer retorna a resposta de uma sessão mono")
    public void answer_AnswerMonoOfSessionWhenSuccessful() {

        BDDMockito.when(sessionService.answerSession(associatedSession))
            .thenReturn(Mono.just(associatedSession));

        BDDMockito.when(sessionValidation.validateCreateAnswer(associatedSession))
            .thenReturn(Mono.just(associatedSession));

        StepVerifier.create(sessionController.answerSession(associatedSessionData))
            .expectSubscription()
            .expectNext(associatedSessionResponse)
            .verifyComplete();
    }

    @Test
    @DisplayName("delete remove um mono")
    public void delete_RemovesMonoOfSessionWhenSuccessful() {

        BDDMockito.when(sessionService.delete("606a4c8b75adf05a9009ae35"))
            .thenReturn(Mono.empty());

        BDDMockito.when(sessionValidation.validateDeleteSession("606a4c8b75adf05a9009ae35"))
            .thenReturn(Mono.just("606a4c8b75adf05a9009ae35"));

        StepVerifier.create(sessionController.delete("606a4c8b75adf05a9009ae35"))
            .expectSubscription()
            .verifyComplete();
    }

    @Test
    @DisplayName("delete gera uma excessão ao tentar remover uma sessão restrita")
    public void delete_RemovesMonoOfSessionWhenRestrict() {

        BDDMockito.when(sessionService.delete("606a4c8b75adf05a9009ae35"))
            .thenReturn(Mono.empty());

        BDDMockito.when(sessionValidation.validateDeleteSession("606a4c8b75adf05a9009ae35"))
            .thenThrow(
                new ConstraintBreakException(
                    ErrorCode.CONSTRAINT_ERROR.getMessage(),
                    ErrorCode.CONSTRAINT_ERROR.getCode()
                )
            );

        assertThrows(ConstraintBreakException.class, () -> {
            sessionController.delete("606a4c8b75adf05a9009ae35");
        });
    }
}
