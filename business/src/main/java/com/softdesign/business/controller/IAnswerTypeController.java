package com.softdesign.business.controller;

import com.softdesign.business.data.AnswerTypeData;
import com.softdesign.business.response.AnswerTypeResponse;
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
import javax.validation.Valid;

public interface IAnswerTypeController {


    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Cria um tipo de resposta")
    Mono<AnswerTypeResponse> save(@RequestBody AnswerTypeData answerTypeData);

    @DeleteMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deleta um tipo de resposta que não esteja vinculado a nada")
    Mono<Void> delete(@PathVariable @Valid String id);

    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Busca todos os tipos de respostas")
    Flux<AnswerTypeResponse> findAll(@RequestParam(value = "page", defaultValue = "0") long page,
                                 @RequestParam(value = "size", defaultValue = "10") long size);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/{id}"
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Busca um tipo de resposta por id")
    Mono<AnswerTypeResponse> findById(@PathVariable @Valid String id);

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/find-by-answer/{answer}"
    )
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Busca um tipo de resposta por resposta")
    Mono<AnswerTypeResponse> findByAnswer(@PathVariable @Valid String answer);
}
