package com.softdesign.business.controller;

import com.softdesign.business.data.AssociatedData;
import com.softdesign.business.response.AssociatedResponse;
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

public interface IAssociatedController {

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Cria associado com cpf válido")
    Mono<AssociatedResponse> save(@RequestBody AssociatedData associatedData);

    @DeleteMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deleta um associado que não esteja vinculado a sessão")
    Mono<Void> delete(@PathVariable String id);

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Busca todos os associados")
    Flux<AssociatedResponse> findAll(@RequestParam(value = "page", defaultValue = "0") long page,
                                     @RequestParam(value = "size", defaultValue = "10") long size);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Busca o associado por id")
    Mono<AssociatedResponse>findById(@PathVariable String id);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/find-by-cpf/{cpf}"
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Busca o associado por cpf")
    Mono<AssociatedResponse> findByCpf(@PathVariable String cpf);
}
