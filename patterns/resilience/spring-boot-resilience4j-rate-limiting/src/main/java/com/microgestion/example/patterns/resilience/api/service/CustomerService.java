package com.microgestion.example.patterns.resilience.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microgestion.example.patterns.resilience.api.exception.ResourceNotFoundException;
import com.microgestion.example.patterns.resilience.api.model.Customer;
import com.microgestion.example.patterns.resilience.api.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {
    @Autowired
    private final CustomerRepository customerRepository;

    // @RateLimiter(name = "customerServiceRateLimit")
    public Flux<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // @RateLimiter(name = "customerServiceRateLimit")
    public Mono<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer", id.toString())));
    }

    // @RateLimiter(name = "customerWriteRateLimit")
    public Mono<Customer> createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // @RateLimiter(name = "customerWriteRateLimit")
    public Mono<Customer> updateCustomer(Long id, Customer customer) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Customer", id.toString())))
                .flatMap(existingCustomer -> {
                    customer.setId(id);
                    return customerRepository.save(customer);
                });
    }

    // @RateLimiter(name = "customerWriteRateLimit")
    public Mono<Void> deleteCustomer(Long id) {
        return customerRepository.deleteById(id);
    }

    // @RateLimiter(name = "customerServiceRateLimit")
    public Flux<Customer> searchCustomersByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }
}
