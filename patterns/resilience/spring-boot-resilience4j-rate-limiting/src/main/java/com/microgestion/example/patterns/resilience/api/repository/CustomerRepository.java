package com.microgestion.example.patterns.resilience.api.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.microgestion.example.patterns.resilience.api.model.Customer;

import reactor.core.publisher.Flux;
// import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {
    Flux<Customer> findByNameContainingIgnoreCase(String name);
}