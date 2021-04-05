package com.softdesign.votingsystem.application.service;

import com.softdesign.business.domain.AssociatedSession;
import com.softdesign.business.domain.Session;
import com.softdesign.business.repository.AssociatedSessionRepository;
import com.softdesign.business.repository.SessionRepository;
import com.softdesign.business.service.SessionService;
import com.softdesign.business.service.impl.SessionServiceImpl;
import com.softdesign.votingsystem.application.util.SessionCreator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
public class SessionServiceTest {

    @InjectMocks
    private SessionService sessionService = new SessionServiceImpl();

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private AssociatedSessionRepository associatedSessionRepository;

    private final Session session = SessionCreator.createDataSession();

    private final AssociatedSession associatedSession = SessionCreator.createDataAssociatedSession();

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
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

        BDDMockito.when(sessionRepository.findAll())
                .thenReturn(Flux.just(session));

        StepVerifier.create(sessionService.findAll())
                .expectSubscription()
                .expectNext(session)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de sessão quando ele existe")
    public void findById_ReturnMonoOfSessionWhenSuccessful() {

        BDDMockito.when(sessionRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(session));

        StepVerifier.create(sessionService.findById("606a4c8b75adf05a9009ae37"))
                .expectSubscription()
                .expectNext(session)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de sessão quando ele não existe")
    public void findById_ReturnMonoOfSessionWhenMonoEmptyIsReturned() {

        BDDMockito.when(sessionRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(sessionService.findById("1"))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("findByTheme retorna um flux de sessão quando ele existe")
    public void findByTheme_ReturnMonoOfSessionWhenSuccessful() {

        BDDMockito.when(sessionRepository.findByTheme(ArgumentMatchers.anyString()))
                .thenReturn(Flux.just(session));

        StepVerifier.create(sessionService.findByTheme(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext(session)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByTheme retorna um flux de sessões quando ele não existe")
    public void findByTheme_ReturnMonoOfSessionWhenMonoEmptyIsReturned() {

        BDDMockito.when(sessionRepository.findByTheme(ArgumentMatchers.anyString()))
                .thenReturn(Flux.empty());

        StepVerifier.create(sessionService.findByTheme(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("save retorna um mono ao salvá-lo")
    public void save_CreatesMonoOfSessionWhenSuccessful() {

        BDDMockito.when(sessionRepository.save(session))
                .thenReturn(Mono.just(session));

        StepVerifier.create(sessionService.save(session))
                .expectSubscription()
                .expectNext(session)
                .verifyComplete();
    }

    @Test
    @DisplayName("answer retorna a resposta de uma sessão mono")
    public void answer_AnswerMonoOfSessionWhenSuccessful() {

        BDDMockito.when(associatedSessionRepository.save(associatedSession))
                .thenReturn(Mono.just(associatedSession));

        StepVerifier.create(sessionService.answerSession(associatedSession))
                .expectSubscription()
                .expectNext(associatedSession)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete remove um mono")
    public void delete_RemovesMonoOfSessionWhenSuccessful() {

        BDDMockito.when(sessionRepository.deleteById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(sessionService.delete("1"))
                .expectSubscription()
                .verifyComplete();
    }
}
