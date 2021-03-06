package com.softdesign.business.controller;

import com.softdesign.business.data.AssociatedSessionData;
import com.softdesign.business.data.SessionData;
import com.softdesign.business.response.AssociatedSessionResponse;
import com.softdesign.business.response.SessionResponse;
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

public interface ISessionController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Cria uma sessão com pauta existente")
    Mono<SessionResponse> save(@RequestBody SessionData sessionData);

    @PostMapping(
        produces = MediaType.APPLICATION_JSON_VALUE,
        value = "/answer"
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Responde uma sessão com um associado, pauta e tipo de resposta válidos")
    Mono<AssociatedSessionResponse> answerSession(@RequestBody AssociatedSessionData associatedSessionData);

    @DeleteMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deleta uma sessão que não seja vinculada ou respondida")
    Mono<Void> delete(@PathVariable String id);

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Busca por todas as sessões")
    Flux<SessionResponse> findAll(@RequestParam(value = "page", defaultValue = "0") long page,
                                 @RequestParam(value = "size", defaultValue = "10") long size);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Busca sessão por id")
    Mono<SessionResponse> findById(@PathVariable String id);
}
