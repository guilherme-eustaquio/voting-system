package com.softdesign.votingsystem.application.controller;

import com.softdesign.business.data.AssociatedData;
import com.softdesign.business.domain.Associated;
import com.softdesign.business.response.AssociatedResponse;
import com.softdesign.business.service.AssociatedService;
import com.softdesign.votingsystem.application.constants.ErrorCode;
import com.softdesign.votingsystem.application.exception.AssociatedAlreadyExistsException;
import com.softdesign.votingsystem.application.exception.AssociatedNotFoundException;
import com.softdesign.votingsystem.application.exception.InvalidCpfException;
import com.softdesign.votingsystem.application.util.AssociatedCreator;
import com.softdesign.votingsystem.application.validation.AssociatedValidation;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
public class AssociatedControllerTest {

    @InjectMocks
    private AssociatedController associatedController;

    @Mock
    private AssociatedService associatedService;

    @Mock
    private AssociatedValidation associatedValidation;

    @Mock
    private ModelMapper modelMapper;

    private final Associated associated = AssociatedCreator.createDataAssociated();
    private final AssociatedResponse associatedResponse = AssociatedCreator.createDataAssociatedResponse();
    private final AssociatedData associatedData = AssociatedCreator.createDataAssociatedData();

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    @BeforeEach
    public void setup() {
        BDDMockito.when(modelMapper.map(associated, AssociatedResponse.class))
                .thenReturn(associatedResponse);

        BDDMockito.when(modelMapper.map(associatedData, Associated.class))
                .thenReturn(associated);
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
    @DisplayName("findAll retorna um flux de associados")
    public void findAll_ReturnFluxOfAssociatedWhenSuccessful() {

        BDDMockito.when(associatedService.findAll())
                .thenReturn(Flux.just(associated));

        StepVerifier.create(associatedController.findAll(0, 10))
                .expectSubscription()
                .expectNext(associatedResponse)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de associado quando ele existe")
    public void findById_ReturnMonoOfAssociatedWhenSuccessful() {

        BDDMockito.when(associatedService.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(associated));

        StepVerifier.create(associatedController.findById("606a4c8b75adf05a9009ae37"))
                .expectSubscription()
                .expectNext(associatedResponse)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de associado quando ele não existe")
    public void findById_ReturnMonoOfAssociatedWhenMonoEmptyIsReturned() {

        BDDMockito.when(associatedService.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(associatedController.findById("1"))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("findByCpf retorna um mono de associado quando ele existe")
    public void findByCpf_ReturnMonoOfAssociatedWhenSuccessful() {

        BDDMockito.when(associatedService.findByCpf(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(associated));

        StepVerifier.create(associatedController.findByCpf(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext(associatedResponse)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByCpf retorna um mono vazio quando ele não existe")
    public void findByCpf_ReturnMonoOfAssociatedWhenMonoEmptyIsReturned() {

        BDDMockito.when(associatedService.findByCpf(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(associatedController.findByCpf(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("save retorna um mono ao salvá-lo")
    public void save_CreatesMonoOfAssociatedWhenSuccessful() {

        BDDMockito.when(associatedService.save(associated))
                .thenReturn(Mono.just(associated));

        BDDMockito.when(associatedValidation.validateCreateAssociated(associated))
                .thenReturn(Mono.just(associated));

        StepVerifier.create(associatedController.save(associatedData))
                .expectSubscription()
                .expectNext(associatedResponse)
                .verifyComplete();
    }

    @Test
    @DisplayName("save ocorre excessão se caso já existir o cadastro do usuário")
    public void save_CreatesMonoOfAssociatedWhenAlreadyExists() {

        BDDMockito.when(associatedValidation.validateCreateAssociated(associated))
        .thenThrow(new AssociatedAlreadyExistsException(
                ErrorCode.ASSOCIATED_ALREADY_EXISTS.getMessage(),
                ErrorCode.ASSOCIATED_ALREADY_EXISTS.getCode()
        ));

        BDDMockito.when(associatedService.save(associated))
                .thenReturn(Mono.just(associated));

        assertThrows(AssociatedAlreadyExistsException.class, () -> {
            associatedController.save(associatedData);
        });
    }

    @Test
    @DisplayName("save ocorre excessão caso o cpf for inválido")
    public void save_CreatesMonoOfAssociatedWhenCpfIsInvalid() {

        BDDMockito.when(associatedValidation.validateCreateAssociated(associated))
                .thenThrow(new InvalidCpfException(
                        ErrorCode.INVALID_CPF.getMessage(),
                        ErrorCode.INVALID_CPF.getCode()
                ));

        BDDMockito.when(associatedService.save(associated))
                .thenReturn(Mono.just(associated));

        assertThrows(InvalidCpfException.class, () -> {
            associatedController.save(associatedData);
        });
    }

    @Test
    @DisplayName("delete remove um mono")
    public void delete_RemovesMonoOfAssociatedWhenSuccessful() {

        BDDMockito.when(associatedService.delete(associated.getId()))
                .thenReturn(Mono.empty());

        BDDMockito.when(associatedValidation.validatedDeleteAssociated(associated.getId()))
                .thenReturn(Mono.empty());

        StepVerifier.create(associatedController.delete("606a4c8b75adf05a9009ae37"))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    @DisplayName("delete ocorre uma excessão ao tentar excluir um associado que não existe")
    public void delete_RemovesMonoOfAssociatedWhenNotFound() {

        BDDMockito.when(associatedService.delete("606a4c8b75adf05a9009ae34"))
                .thenReturn(Mono.empty());

        BDDMockito.when(associatedValidation.validatedDeleteAssociated("606a4c8b75adf05a9009ae34"))
                .thenThrow(new AssociatedNotFoundException(
                        ErrorCode.ASSOCIATED_NOT_FOUND.getMessage(),
                        ErrorCode.ASSOCIATED_NOT_FOUND.getCode()
                ));

        assertThrows(AssociatedNotFoundException.class, () -> {
            associatedController.delete("606a4c8b75adf05a9009ae34");
        });
    }

    @Test
    @DisplayName("delete ocorre uma excessão ao tentar excluir um associado restrito")
    public void delete_RemovesMonoOfAssociatedWhenRestricted() {

        BDDMockito.when(associatedService.delete("606a4c8b75adf05a9009ae34"))
                .thenReturn(Mono.empty());

        BDDMockito.when(associatedValidation.validatedDeleteAssociated("606a4c8b75adf05a9009ae34"))
                .thenThrow(new AssociatedNotFoundException(
                        ErrorCode.ASSOCIATED_NOT_FOUND.getMessage(),
                        ErrorCode.ASSOCIATED_NOT_FOUND.getCode()
                ));

        assertThrows(AssociatedNotFoundException.class, () -> {
            associatedController.delete("606a4c8b75adf05a9009ae34");
        });
    }

}
