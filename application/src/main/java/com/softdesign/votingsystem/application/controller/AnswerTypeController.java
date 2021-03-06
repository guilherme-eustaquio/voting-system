package com.softdesign.votingsystem.application.controller;

import com.softdesign.business.controller.IAnswerTypeController;
import com.softdesign.business.data.AnswerTypeData;
import com.softdesign.business.domain.AnswerType;
import com.softdesign.business.response.AnswerTypeResponse;
import com.softdesign.business.service.AnswerTypeService;
import com.softdesign.votingsystem.application.validation.AnswerTypeValidation;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/answer-types")
@Api(value = "API de gerenciamento do tipo de resposta")
public class AnswerTypeController implements IAnswerTypeController {

    @Autowired
    private AnswerTypeService answerTypeService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AnswerTypeValidation answerTypeValidation;

    @Override
    public Mono<AnswerTypeResponse> save(@RequestBody AnswerTypeData answerTypeData) {

        AnswerType answerTypeToSave = modelMapper.map(answerTypeData, AnswerType.class);

        return answerTypeValidation.validateCreateAnswer(answerTypeToSave)
            .then(answerTypeService.save(answerTypeToSave))
            .map(answerType -> modelMapper.map(answerType, AnswerTypeResponse.class));
    }

    @Override
    public Mono<Void> delete(String id) {
        return answerTypeValidation.validateDeleteAnswerType(id).then(answerTypeService.delete(id));
    }

    @Override
    public Flux<AnswerTypeResponse> findAll(long page, long size) {
        return answerTypeService.findAll().map(answerType -> modelMapper.map(answerType, AnswerTypeResponse.class))
                .skip(page * size).take(size);
    }

    @Override
    public Mono<AnswerTypeResponse> findById(String id) {
        return answerTypeService.findById(id).map(answerType -> modelMapper.map(answerType, AnswerTypeResponse.class));
    }

    @Override
    public Mono<AnswerTypeResponse> findByAnswer(String answer) {
        return answerTypeService.findByAnswer(answer).map(answerType -> modelMapper.map(answerType, AnswerTypeResponse.class));
    }
}
