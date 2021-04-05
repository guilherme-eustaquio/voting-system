package com.softdesign.votingsystem.application.service;

import com.softdesign.business.domain.Associated;
import com.softdesign.business.repository.AssociatedRepository;
import com.softdesign.business.service.AssociatedService;
import com.softdesign.business.service.impl.AssociatedServiceImpl;
import com.softdesign.votingsystem.application.util.AssociatedCreator;
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
public class AssociatedServiceTest {

    @InjectMocks
    private AssociatedService associatedService = new AssociatedServiceImpl();

    @Mock
    private AssociatedRepository associatedRepository;

    private final Associated associated = AssociatedCreator.createDataAssociated();

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
    @DisplayName("findAll retorna um flux de associados")
    public void findAll_ReturnFluxOfAssociatedWhenSuccessful() {

        BDDMockito.when(associatedRepository.findAll())
                .thenReturn(Flux.just(associated));

        StepVerifier.create(associatedService.findAll())
                .expectSubscription()
                .expectNext(associated)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de associado quando ele existe")
    public void findById_ReturnMonoOfAssociatedWhenSuccessful() {

        BDDMockito.when(associatedRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(associated));

        StepVerifier.create(associatedService.findById("606a4c8b75adf05a9009ae37"))
                .expectSubscription()
                .expectNext(associated)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de associado quando ele não existe")
    public void findById_ReturnMonoOfAssociatedWhenMonoEmptyIsReturned() {

        BDDMockito.when(associatedRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(associatedService.findById("1"))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("findByCpf retorna um mono de associado quando ele existe")
    public void findByCpf_ReturnMonoOfAssociatedWhenSuccessful() {

        BDDMockito.when(associatedRepository.findByCpf(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(associated));

        StepVerifier.create(associatedService.findByCpf(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext(associated)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByCpf retorna um mono vazio quando ele não existe")
    public void findByCpf_ReturnMonoOfAssociatedWhenMonoEmptyIsReturned() {

        BDDMockito.when(associatedRepository.findByCpf(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(associatedService.findByCpf(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("save retorna um mono ao salvá-lo")
    public void save_CreatesMonoOfAssociatedWhenSuccessful() {

        BDDMockito.when(associatedRepository.save(associated))
                .thenReturn(Mono.just(associated));

        StepVerifier.create(associatedService.save(associated))
                .expectSubscription()
                .expectNext(associated)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete remove um mono")
    public void delete_RemovesMonoOfAssociatedWhenSuccessful() {

        BDDMockito.when(associatedRepository.deleteById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(associatedService.delete("1"))
                .expectSubscription()
                .verifyComplete();
    }



}
