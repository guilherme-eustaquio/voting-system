package com.softdesign.business.controller;

import com.softdesign.business.data.ThemeData;
import com.softdesign.business.response.ThemeResponse;
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
    Mono<ThemeResponse> save(@RequestBody ThemeData themeData);

    @DeleteMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<Void> delete(@PathVariable String id);

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    Flux<ThemeResponse> findAll(@RequestParam(value = "page", defaultValue = "0") long page,
                                 @RequestParam(value = "size", defaultValue = "10") long size);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.OK)
    Mono<ThemeResponse> findById(@PathVariable String id);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/find-by-question/{question}"
    )
    @ResponseStatus(HttpStatus.OK)
    Mono<ThemeResponse> findByQuestion(@PathVariable String question);
}
