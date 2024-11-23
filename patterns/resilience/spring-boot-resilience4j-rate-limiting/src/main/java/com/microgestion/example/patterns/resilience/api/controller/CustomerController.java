package com.microgestion.example.patterns.resilience.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microgestion.example.patterns.resilience.api.model.Customer;
import com.microgestion.example.patterns.resilience.api.service.CustomerService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Customer management APIs")
public class CustomerController {
    @Autowired
    private final CustomerService customerService;

    @Operation(summary = "Get all customers", description = "Retrieves a list of all customers in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrievedcustomers", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests - ratelimit exceeded")
    })
    @RateLimiter(name = "customerServiceRateLimit")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @Operation(summary = "Get customer by ID", description = "Retrieves a specific customer by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found", content = @Content(schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "429", description = "Too many requests - rate limit exceeded")
    })
    @GetMapping("/{id}")
    @RateLimiter(name = "customerServiceRateLimit")
    public Mono<Customer> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @Operation(summary = "Create new customer", description = "Creates a new customer in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully", content = @Content(schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "429", description = "Too many requests - rate limit exceeded")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RateLimiter(name = "customerWriteRateLimit")
    public Mono<Customer> createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @Operation(summary = "Update customer", description = "Updates an existing customer's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully", content = @Content(schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "429", description = "Too many requests - rate limit exceeded")
    })
    @PutMapping("/{id}")
    @RateLimiter(name = "customerWriteRateLimit")
    public Mono<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    @Operation(summary = "Delete customer", description = "Deletes a customer from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "429", description = "Too many requests - rate limit exceeded")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RateLimiter(name = "customerWriteRateLimit")
    public Mono<Void> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id);
    }

    @Operation(summary = "Search customers by name", description = "Searches for customers whose names contain the given search term")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully", content = @Content(schema = @Schema(implementation = Customer.class))),
            @ApiResponse(responseCode = "429", description = "Too many requests - rate limit exceeded")
    })
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @RateLimiter(name = "customerServiceRateLimit")
    public Flux<Customer> searchCustomers(@RequestParam String name) {
        return customerService.searchCustomersByName(name);
    }
}