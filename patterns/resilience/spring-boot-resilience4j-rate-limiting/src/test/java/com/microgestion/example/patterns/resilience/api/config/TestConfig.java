package com.microgestion.example.patterns.resilience.api.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.microgestion.example.patterns.resilience.api.model.Customer;
import com.microgestion.example.patterns.resilience.api.repository.CustomerRepository;
import com.microgestion.example.patterns.resilience.api.service.CustomerService;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@TestConfiguration
public class TestConfig {

    private final Map<Long, Customer> customerDb = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(2);

    public TestConfig() {
        // Initialize test data
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("John Doe");
        customer1.setEmail("john.doe@example.com");
        customer1.setPhone("+1234567890");

        Customer customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Jane Smith");
        customer2.setEmail("jane.smith@example.com");
        customer2.setPhone("+1987654321");

        customerDb.put(customer1.getId(), customer1);
        customerDb.put(customer2.getId(), customer2);
    }

    @Bean
    @Primary
    public RateLimiter testRateLimiter() {
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(2)
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .timeoutDuration(Duration.ofMillis(500))
                .build();

        return RateLimiter.of("customerService", config);
    }

    @Bean
    @Primary
    public CustomerRepository customerRepository() {
        CustomerRepository mockRepo = Mockito.mock(CustomerRepository.class);

        // findAll
        Mockito.when(mockRepo.findAll())
                .thenAnswer(invocation -> Flux.fromIterable(customerDb.values()));

        // findById
        Mockito.when(mockRepo.findById(Mockito.any(Long.class)))
                .thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0);
                    return customerDb.containsKey(id) ? Mono.just(customerDb.get(id)) : Mono.empty();
                });

        // save (for both create and update)
        Mockito.when(mockRepo.save(Mockito.any(Customer.class)))
                .thenAnswer(invocation -> {
                    Customer customer = invocation.getArgument(0);
                    if (customer.getId() == null) {
                        customer.setId(idGenerator.incrementAndGet());
                    }
                    customerDb.put(customer.getId(), customer);
                    return Mono.just(customer);
                });

        // deleteById
        Mockito.when(mockRepo.deleteById(Mockito.any(Long.class)))
                .thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0);
                    customerDb.remove(id);
                    return Mono.empty();
                });

        // findByNameContainingIgnoreCase
        Mockito.when(mockRepo.findByNameContainingIgnoreCase(Mockito.anyString()))
                .thenAnswer(invocation -> {
                    String searchTerm = invocation.getArgument(0).toString().toLowerCase();
                    return Flux.fromIterable(customerDb.values())
                            .filter(customer -> customer.getName().toLowerCase().contains(searchTerm));
                });

        return mockRepo;
    }

    @Bean
    @Primary
    public CustomerService customerService(CustomerRepository customerRepository, RateLimiter rateLimiter) {
        return new CustomerService(customerRepository);
    }
}