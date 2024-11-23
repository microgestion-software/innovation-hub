package com.microgestion.example.patterns.resilience.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Table("customers")
@Schema(description = "Customer entity representing a client in the system")
public class Customer {
    @Id
    @Schema(description = "Unique identifier of the customer", example = "1")
    private Long id;

    @Schema(description = "Customer's full name", example = "John Doe", required = true)
    private String name;

    @Schema(description = "Customer's email address", example = "john.doe@example.com", required = true)
    private String email;

    @Schema(description = "Customer's phone number", example = "+1234567890")
    private String phone;
}