package com.softdesign.votingsystem.application.service;

import com.softdesign.business.domain.AnswerType;
import com.softdesign.business.repository.AnswerTypeRepository;
import com.softdesign.business.service.AnswerTypeService;
import com.softdesign.business.service.impl.AnswerTypeServiceImpl;
import com.softdesign.votingsystem.application.util.AnswerTypeCreator;
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
public class AnswerTypeServiceTest {

    @InjectMocks
    private AnswerTypeService answerTypeService = new AnswerTypeServiceImpl();

    @Mock
    private AnswerTypeRepository answerTypeRepository;

    private final AnswerType answerType = AnswerTypeCreator.createDataAnswerType();

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
    @DisplayName("findAll retorna um flux de answerTypes")
    public void findAll_ReturnFluxOfAnswerTypeWhenSuccessful() {

        BDDMockito.when(answerTypeRepository.findAll())
                .thenReturn(Flux.just(answerType));

        StepVerifier.create(answerTypeService.findAll())
            .expectSubscription()
            .expectNext(answerType)
            .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de answerType quando ele existe")
    public void findById_ReturnMonoOfAnswerTypeWhenSuccessful() {

        BDDMockito.when(answerTypeRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(answerType));

        StepVerifier.create(answerTypeService.findById("606a4c8b75adf05a9009ae37"))
                .expectSubscription()
                .expectNext(answerType)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de answerType quando ele não existe")
    public void findById_ReturnMonoOfAnswerTypeWhenMonoEmptyIsReturned() {

        BDDMockito.when(answerTypeRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(answerTypeService.findById("1"))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("findByAnswer retorna um mono de answerType quando ele existe")
    public void findByAnswer_ReturnMonoOfAnswerTypeWhenSuccessful() {

        BDDMockito.when(answerTypeRepository.findByAnswer(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(answerType));

        StepVerifier.create(answerTypeService.findByAnswer(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext(answerType)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByAnswer retorna um mono de answerType quando ele não existe")
    public void findByAnswer_ReturnMonoOfAnswerTypeWhenMonoEmptyIsReturned() {

        BDDMockito.when(answerTypeRepository.findByAnswer(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(answerTypeService.findByAnswer(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("save retorna um mono ao salvá-lo")
    public void save_CreatesMonoOfAnswerTypeWhenSuccessful() {

        BDDMockito.when(answerTypeRepository.save(answerType))
                .thenReturn(Mono.just(answerType));

        StepVerifier.create(answerTypeService.save(answerType))
                .expectSubscription()
                .expectNext(answerType)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete remove um mono")
    public void delete_RemovesMonoOfAnswerTypeWhenSuccessful() {

        BDDMockito.when(answerTypeRepository.deleteById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(answerTypeService.delete("1"))
                .expectSubscription()
                .verifyComplete();
    }



}
