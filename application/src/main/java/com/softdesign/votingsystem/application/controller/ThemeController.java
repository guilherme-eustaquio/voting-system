package com.softdesign.votingsystem.application.controller;

import com.softdesign.business.controller.IThemeController;
import com.softdesign.business.data.ThemeData;
import com.softdesign.business.domain.Theme;
import com.softdesign.business.response.ThemeResponse;
import com.softdesign.business.service.ThemeService;
import com.softdesign.votingsystem.application.validation.ThemeValidation;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/themes")
@Api(value = "API de gerenciamento de pautas")
public class ThemeController implements IThemeController {

    @Autowired
    ThemeService themeService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ThemeValidation themeValidation;

    @Override
    public Mono<ThemeResponse> save(ThemeData themeData) {
        Theme themeToSave = modelMapper.map(themeData, Theme.class);
        return themeService.save(themeToSave).map(theme -> modelMapper.map(theme, ThemeResponse.class));
    }

    @Override
    public Mono<Void> delete(String id) {
        return themeValidation.validateDeleteTheme(id).then(themeService.delete(id));
    }

    @Override
    public Flux<ThemeResponse> findAll(long page, long size) {
        return themeService.findAll().map(theme -> modelMapper.map(theme, ThemeResponse.class))
                .skip(page * size).take(size);
    }

    @Override
    public Mono<ThemeResponse> findById(String id) {
        return themeService.findById(id).map(theme -> modelMapper.map(theme, ThemeResponse.class));
    }

    @Override
    public Mono<ThemeResponse> findByQuestion(String question) {
        return themeService.findByQuestion(question).map(theme -> modelMapper.map(theme, ThemeResponse.class));
    }
}
