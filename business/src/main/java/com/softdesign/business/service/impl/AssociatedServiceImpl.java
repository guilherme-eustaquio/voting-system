package com.softdesign.business.service.impl;

import com.softdesign.business.domain.Associated;
import com.softdesign.business.repository.AssociatedRepository;
import com.softdesign.business.service.AssociatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class AssociatedServiceImpl implements AssociatedService {

    @Autowired
    private AssociatedRepository associatedRepository;

    @Override
    public Flux<Associated> findAll() {
        return associatedRepository.findAll();
    }

    @Override
    public Mono<Associated> findById(String id) { return associatedRepository.findById(id); }

    @Override
    public Mono<Associated> findByCpf(String cpf) { return associatedRepository.findByCpf(cpf); }

    @Override
    public Mono<Associated> save(Associated associated) {
        associated.setCreatedAt(LocalDateTime.now());
        return associatedRepository.save(associated);
    }

    @Override
    public Mono<Void> delete(String id) {
        return associatedRepository.deleteById(id);
    }
}
