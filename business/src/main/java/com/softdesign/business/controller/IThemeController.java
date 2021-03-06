package com.softdesign.business.controller;

import com.softdesign.business.data.ThemeData;
import com.softdesign.business.response.ThemeResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IThemeController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Cria uma pauta")
    Mono<ThemeResponse> save(@RequestBody ThemeData themeData);

    @DeleteMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deleta uma pauta não vinculada a uma sessão")
    Mono<Void> delete(@PathVariable String id);

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Busca por todas as pautas")
    Flux<ThemeResponse> findAll(@RequestParam(value = "page", defaultValue = "0") long page,
                                 @RequestParam(value = "size", defaultValue = "10") long size);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Busca pauta por id")
    Mono<ThemeResponse> findById(@PathVariable String id);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/find-by-question/{question}"
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Busca pauta por questão")
    Mono<ThemeResponse> findByQuestion(@PathVariable String question);
}
