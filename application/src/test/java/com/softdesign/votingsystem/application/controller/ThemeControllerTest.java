package com.softdesign.votingsystem.application.controller;

import com.softdesign.business.data.ThemeData;
import com.softdesign.business.domain.Theme;
import com.softdesign.business.response.ThemeResponse;
import com.softdesign.business.service.ThemeService;
import com.softdesign.votingsystem.application.constants.ErrorCode;
import com.softdesign.votingsystem.application.exception.ConstraintBreakException;
import com.softdesign.votingsystem.application.util.ThemeCreator;
import com.softdesign.votingsystem.application.validation.ThemeValidation;
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
public class ThemeControllerTest {

    @InjectMocks
    private ThemeController themeController;

    @Mock
    private ThemeService themeService;

    @Mock
    private ThemeValidation themeValidation;

    private final Theme theme = ThemeCreator.createDataTheme();

    private final ThemeResponse themeResponse = ThemeCreator.createDataThemeResponse();

    private final ThemeData themeData = ThemeCreator.createDataThemeData();

    @Mock
    private ModelMapper modelMapper;

    @BeforeAll
    public static void blockHoundSetup() {
        BlockHound.install();
    }

    @BeforeEach
    public void setup() {

        BDDMockito.when(modelMapper.map(theme, ThemeResponse.class))
            .thenReturn(themeResponse);

        BDDMockito.when(modelMapper.map(themeData, Theme.class))
            .thenReturn(theme);

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

        BDDMockito.when(themeService.findAll())
                .thenReturn(Flux.just(theme));

        StepVerifier.create(themeController.findAll(0, 10))
                .expectSubscription()
                .expectNext(themeResponse)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de theme quando ele existe")
    public void findById_ReturnMonoOfThemeWhenSuccessful() {

        BDDMockito.when(themeService.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(theme));

        StepVerifier.create(themeController.findById("606a4c8b75adf05a9009ae37"))
                .expectSubscription()
                .expectNext(themeResponse)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById retorna um mono de theme quando ele não existe")
    public void findById_ReturnMonoOfThemeWhenMonoEmptyIsReturned() {

        BDDMockito.when(themeService.findById(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(themeController.findById("1"))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("findByQuestion retorna um mono de theme quando ele existe")
    public void findByQuestion_ReturnMonoOfThemeWhenSuccessful() {

        BDDMockito.when(themeService.findByQuestion(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(theme));

        StepVerifier.create(themeController.findByQuestion(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext(themeResponse)
                .verifyComplete();
    }

    @Test
    @DisplayName("findByQuestion retorna um mono de theme quando ele não existe")
    public void findByQuestion_ReturnMonoOfThemeWhenMonoEmptyIsReturned() {

        BDDMockito.when(themeService.findByQuestion(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(themeController.findByQuestion(ArgumentMatchers.anyString()))
                .expectSubscription()
                .expectNext()
                .verifyComplete();
    }

    @Test
    @DisplayName("save retorna um mono ao salvá-lo")
    public void save_CreatesMonoOfThemeWhenSuccessful() {

        BDDMockito.when(themeService.save(theme))
            .thenReturn(Mono.just(theme));

        StepVerifier.create(themeController.save(themeData))
            .expectSubscription()
            .expectNext(themeResponse)
            .verifyComplete();
    }

    @Test
    @DisplayName("delete remove um mono")
    public void delete_RemovesMonoOfThemeWhenSuccessful() {

        BDDMockito.when(themeValidation.validateDeleteTheme(ArgumentMatchers.anyString()))
            .thenReturn(Mono.just("606a4c8b75adf05a9009ae35"));

        BDDMockito.when(themeService.delete("606a4c8b75adf05a9009ae35"))
            .thenReturn(Mono.empty());

        StepVerifier.create(themeController.delete("606a4c8b75adf05a9009ae35"))
            .expectSubscription()
            .verifyComplete();
    }

    @Test
    @DisplayName("delete gera uma excessão caso o tema seja restrito")
    public void delete_RemovesMonoOfThemeWhenRestrict() {

        BDDMockito.when(themeValidation.validateDeleteTheme(ArgumentMatchers.anyString()))
            .thenThrow(
                new ConstraintBreakException(
                    ErrorCode.CONSTRAINT_ERROR.getMessage(),
                    ErrorCode.CONSTRAINT_ERROR.getCode()
                )
            );

        BDDMockito.when(themeService.delete(ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());

        assertThrows(ConstraintBreakException.class, () -> {
            themeController.delete("606a4c8b75adf05a9009ae35");
        });
    }
}
