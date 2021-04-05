package com.softdesign.votingsystem.application.service;

import com.softdesign.business.domain.Theme;
import com.softdesign.business.repository.ThemeRepository;
import com.softdesign.business.service.ThemeService;
import com.softdesign.business.service.impl.ThemeServiceImpl;
import com.softdesign.votingsystem.application.util.ThemeCreator;
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
public class ThemeServiceTest {

    @InjectMocks
    private ThemeService themeService = new ThemeServiceImpl();

    @Mock
    private ThemeRepository themeRepository;

    private final Theme theme = ThemeCreator.createDataTheme();

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
    @DisplayName("findAll retorna um flux de themes")
    public void findAll_ReturnFluxOfThemeWhenSuccessful() {

        BDDMockito.when(themeRepository.findAll())
                .thenReturn(Flux.just(theme));

        StepVerifier.create(themeService.findAll())
                .expectSubscription()
                .expectNext(theme)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de theme quando ele existe")
    public void findById_ReturnMonoOfThemeWhenSuccessful() {

        BDDMockito.when(themeRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(theme));

        StepVerifier.create(themeService.findById("606a4c8b75adf05a9009ae37"))
                .expectSubscription()
                .expectNext(theme)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de theme quando ele não existe")
    public void findById_ReturnMonoOfThemeWhenMonoEmptyIsReturned() {

        BDDMockito.when(themeRepository.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(themeService.findById("1"))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("findByQuestion retorna um mono de theme quando ele existe")
    public void findByQuestion_ReturnMonoOfThemeWhenSuccessful() {

        BDDMockito.when(themeRepository.findByQuestion(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(theme));

        StepVerifier.create(themeService.findByQuestion(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext(theme)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByQuestion retorna um mono de theme quando ele não existe")
    public void findByQuestion_ReturnMonoOfThemeWhenMonoEmptyIsReturned() {

        BDDMockito.when(themeRepository.findByQuestion(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(themeService.findByQuestion(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("save retorna um mono ao salvá-lo")
    public void save_CreatesMonoOfThemeWhenSuccessful() {

        BDDMockito.when(themeRepository.save(theme))
                .thenReturn(Mono.just(theme));

        StepVerifier.create(themeService.save(theme))
                .expectSubscription()
                .expectNext(theme)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete remove um mono")
    public void delete_RemovesMonoOfThemeWhenSuccessful() {

        BDDMockito.when(themeRepository.deleteById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(themeService.delete("1"))
                .expectSubscription()
                .verifyComplete();
    }



}
