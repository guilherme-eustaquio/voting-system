package com.softdesign.business.repository;

import com.softdesign.business.domain.Associated;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AssociatedRepository extends ReactiveCrudRepository<Associated, String> {
    Mono<Associated> findByCpf(String cpf);
}
