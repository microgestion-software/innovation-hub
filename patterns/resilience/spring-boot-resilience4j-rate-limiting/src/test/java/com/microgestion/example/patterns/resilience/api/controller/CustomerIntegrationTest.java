package com.microgestion.example.patterns.resilience.api.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.microgestion.example.patterns.resilience.api.config.TestConfig;
import com.microgestion.example.patterns.resilience.api.model.Customer;

@WebFluxTest(CustomerController.class)
@Import(TestConfig.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testCustomerCRUDOperations() {
        // Create new customer
        Customer newCustomer = new Customer();
        newCustomer.setName("Test Customer");
        newCustomer.setEmail("test@example.com");
        newCustomer.setPhone("+1111111111");

        // Create
        Customer created = webTestClient.post()
                .uri("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newCustomer)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Customer.class)
                .returnResult()
                .getResponseBody();

        if (created != null) {
            // Read
            webTestClient.get()
                    .uri("/api/customers/{id}", created.getId())
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.name").isEqualTo("Test Customer");

            // Update
            created.setName("Updated Customer");
            webTestClient.put()
                    .uri("/api/customers/{id}", created.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(created)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.name").isEqualTo("Updated Customer");

            // Delete
            webTestClient.delete()
                    .uri("/api/customers/{id}", created.getId())
                    .exchange()
                    .expectStatus().isNoContent();
        }
    }
}