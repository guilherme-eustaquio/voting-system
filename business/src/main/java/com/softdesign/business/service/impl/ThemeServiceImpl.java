package com.softdesign.business.service.impl;

import com.softdesign.business.domain.Theme;
import com.softdesign.business.repository.ThemeRepository;
import com.softdesign.business.service.ThemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ThemeServiceImpl implements ThemeService {

    @Autowired
    private ThemeRepository themeRepository;

    @Override
    public Flux<Theme> findAll() {
        return themeRepository.findAll();
    }

    @Override
    public Mono<Theme> findById(String id) { return themeRepository.findById(id); }

    @Override
    public Mono<Theme> findByQuestion(String question) { return themeRepository.findByQuestion(question); }

    @Override
    public Mono<Theme> save(Theme theme) {
        theme.setCreatedAt(LocalDateTime.now());
        return themeRepository.save(theme);
    }

    @Override
    public Mono<Void> delete(String id) {
        return themeRepository.deleteById(id);
    }
}
