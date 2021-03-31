package com.softdesign.votingsystem.application.controller;

import com.softdesign.business.controller.IAssociatedController;
import com.softdesign.business.data.AssociatedData;
import com.softdesign.business.domain.Associated;
import com.softdesign.business.response.AssociatedResponse;
import com.softdesign.business.service.AssociatedService;
import com.softdesign.votingsystem.application.validation.AssociatedValidation;
import io.swagger.annotations.Api;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/associates")
@Api(value = "API de gerenciamento de associados")
public class AssociatedController implements IAssociatedController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssociatedService associatedService;

    @Autowired
    private AssociatedValidation associatedValidation;

    @Override
    public Mono<AssociatedResponse> save(AssociatedData associatedData) {
        Associated associatedToSave = modelMapper.map(associatedData, Associated.class);
        return associatedValidation.validateCreateAssociated(associatedToSave)
            .then(associatedService.save(associatedToSave))
            .map(associated -> modelMapper.map(associated, AssociatedResponse.class));
    }

    @Override
    public Mono<Void> delete(String id) {
        return associatedValidation.validatedDeleteAssociated(id)
            .then(associatedService.delete(id));
    }

    @Override
    public Flux<AssociatedResponse> findAll(long page, long size) {
        return associatedService.findAll().map(associated -> modelMapper.map(associated, AssociatedResponse.class))
            .skip(page * size).take(size);
    }

    @Override
    public Mono<AssociatedResponse> findById(String id) {
        return associatedService.findById(id).map(associated -> modelMapper.map(associated, AssociatedResponse.class));
    }

    @Override
    public Mono<AssociatedResponse> findByCpf(String cpf) {
        return associatedService.findByCpf(cpf).map(associated -> modelMapper.map(associated, AssociatedResponse.class));
    }
}
