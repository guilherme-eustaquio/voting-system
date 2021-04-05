package com.softdesign.votingsystem.application.controller;

import com.softdesign.business.data.AnswerTypeData;
import com.softdesign.business.domain.AnswerType;
import com.softdesign.business.response.AnswerTypeResponse;
import com.softdesign.business.service.AnswerTypeService;
import com.softdesign.votingsystem.application.constants.ErrorCode;
import com.softdesign.votingsystem.application.exception.AnswerTypeAlreadyExistsException;
import com.softdesign.votingsystem.application.exception.AnswerTypeNotFoundException;
import com.softdesign.votingsystem.application.exception.ConstraintBreakException;
import com.softdesign.votingsystem.application.util.AnswerTypeCreator;
import com.softdesign.votingsystem.application.validation.AnswerTypeValidation;
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
public class AnswerTypeControllerTest {

    @InjectMocks
    private AnswerTypeController answerTypeController;

    @Mock
    private AnswerTypeService answerTypeService;

    @Mock
    private AnswerTypeValidation answerTypeValidation;

    @Mock
    private ModelMapper modelMapper;

    private final AnswerType answerType = AnswerTypeCreator.createDataAnswerType();
    private final AnswerTypeResponse answerTypeResponse = AnswerTypeCreator.createDataAnswerTypeResponse();
    private final AnswerTypeData answerTypeData = AnswerTypeCreator.createDataAnswerTypeData();

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

    @BeforeEach
    public void setup() {
        BDDMockito.when(modelMapper.map(answerType, AnswerTypeResponse.class))
                .thenReturn(answerTypeResponse);

        BDDMockito.when(modelMapper.map(answerTypeData, AnswerType.class))
                .thenReturn(answerType);
    }

    @Test
    @DisplayName("findAll retorna um flux de answerTypes")
    public void findAll_ReturnFluxOfAnswerTypeWhenSuccessful() {

        BDDMockito.when(answerTypeService.findAll())
            .thenReturn(Flux.just(answerType));

        StepVerifier.create(answerTypeController.findAll(0, 10))
            .expectSubscription()
            .expectNext(answerTypeResponse)
            .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de answerType quando ele existe")
    public void findById_ReturnMonoOfAnswerTypeWhenSuccessful() {

        BDDMockito.when(answerTypeService.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(answerType));

        StepVerifier.create(answerTypeController.findById("606a4c8b75adf05a9009ae37"))
                .expectSubscription()
                .expectNext(answerTypeResponse)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de answerType quando ele não existe")
    public void findById_ReturnMonoOfAnswerTypeWhenMonoEmptyIsReturned() {

        BDDMockito.when(answerTypeService.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(answerTypeController.findById("1"))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("findByAnswer retorna um mono de answerType quando ele existe")
    public void findByAnswer_ReturnMonoOfAnswerTypeWhenSuccessful() {

        BDDMockito.when(answerTypeService.findByAnswer(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(answerType));

        StepVerifier.create(answerTypeService.findByAnswer(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext(answerType)
                .verifyComplete();
    }


    @Test
    @DisplayName("findByAnswer retorna um mono de answerType quando ele não existe")
    public void findByAnswer_ReturnMonoOfAnswerTypeWhenMonoEmptyIsReturned() {

        BDDMockito.when(answerTypeService.findByAnswer(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(answerTypeController.findByAnswer(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("save retorna um mono ao salvá-lo")
    public void save_CreatesMonoOfAnswerTypeWhenSuccessful() {

        BDDMockito.when(answerTypeService.save(answerType))
                .thenReturn(Mono.just(answerType));

        BDDMockito.when(answerTypeValidation.validateCreateAnswer(answerType))
                .thenReturn(Mono.just(answerType));

        StepVerifier.create(answerTypeController.save(answerTypeData))
                .expectSubscription()
                .expectNext(answerTypeResponse)
                .verifyComplete();
    }

    @Test
    @DisplayName("save ocorre excessão se caso já existir o tipo de resposta")
    public void save_CreatesMonoOfAnswerTypeWhenAlreadyExists() {

        BDDMockito.when(answerTypeValidation.validateCreateAnswer(answerType))
            .thenThrow(new AnswerTypeAlreadyExistsException(
                    ErrorCode.ANSWER_ALREADY_EXISTS.getMessage(),
                    ErrorCode.ANSWER_ALREADY_EXISTS.getCode()
            ));

        BDDMockito.when(answerTypeService.save(answerType))
                .thenReturn(Mono.just(answerType));

        assertThrows(AnswerTypeAlreadyExistsException.class, () -> {
            answerTypeController.save(answerTypeData);
        });
    }

    @Test
    @DisplayName("delete remove um mono")
    public void delete_RemovesMonoOfAnswerTypeWhenSuccessful() {

        BDDMockito.when(answerTypeService.delete(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        BDDMockito.when(answerTypeValidation.validateDeleteAnswerType("606a4c8b75adf05a9009ae34"))
                .thenReturn(Mono.empty());

        StepVerifier.create(answerTypeService.delete("1"))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete ocorre uma excessão em caso do tipo de resposta não existir")
    public void delete_RemovesMonoOfAnswerTypeWhenNotFound() {

        BDDMockito.when(answerTypeService.delete("606a4c8b75adf05a9009ae34"))
                .thenReturn(Mono.empty());

        BDDMockito.when(answerTypeValidation.validateDeleteAnswerType("606a4c8b75adf05a9009ae34"))
                .thenThrow(new AnswerTypeNotFoundException(
                        ErrorCode.ANSWER_TYPE_NOT_FOUND.getMessage(),
                        ErrorCode.ANSWER_TYPE_NOT_FOUND.getCode()
                ));

        assertThrows(AnswerTypeNotFoundException.class, () -> {
            answerTypeController.delete("606a4c8b75adf05a9009ae34");
        });
    }

    @Test
    @DisplayName("delete ocorre uma excessão em caso do tipo de resposta ser restrito")
    public void delete_RemovesMonoOfAnswerTypeWhenRestricted() {

        BDDMockito.when(answerTypeService.delete("606a4c8b75adf05a9009ae34"))
                .thenReturn(Mono.empty());

        BDDMockito.when(answerTypeValidation.validateDeleteAnswerType("606a4c8b75adf05a9009ae34"))
                .thenThrow(new ConstraintBreakException(
                        ErrorCode.CONSTRAINT_ERROR.getMessage(),
                        ErrorCode.CONSTRAINT_ERROR.getCode()
                ));

        assertThrows(ConstraintBreakException.class, () -> {
            answerTypeController.delete("606a4c8b75adf05a9009ae34");
        });
    }



}
