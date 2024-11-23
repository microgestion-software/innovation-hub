package com.microgestion.example.patterns.resilience.api.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.microgestion.example.patterns.resilience.api.model.Customer;
import com.microgestion.example.patterns.resilience.api.repository.CustomerRepository;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Primary
public class TestCustomerService extends CustomerService {
    private final CustomerRepository customerRepository;
    // private final RateLimiter rateLimiter;

    public TestCustomerService(CustomerRepository customerRepository, RateLimiter rateLimiter) {
        super(customerRepository);
        this.customerRepository = customerRepository;
        // this.rateLimiter = rateLimiter;
    }

    @Override
    @RateLimiter(name = "customerService")
    public Flux<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    @RateLimiter(name = "customerService")
    public Mono<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    @RateLimiter(name = "customerService")
    public Mono<Customer> createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    @RateLimiter(name = "customerService")
    public Mono<Customer> updateCustomer(Long id, Customer customer) {
        return customerRepository.findById(id)
                .flatMap(existingCustomer -> {
                    customer.setId(id);
                    return customerRepository.save(customer);
                });
    }

    @Override
    @RateLimiter(name = "customerService")
    public Mono<Void> deleteCustomer(Long id) {
        return customerRepository.deleteById(id);
    }

    @Override
    @RateLimiter(name = "customerService")
    public Flux<Customer> searchCustomersByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name);
    }
}