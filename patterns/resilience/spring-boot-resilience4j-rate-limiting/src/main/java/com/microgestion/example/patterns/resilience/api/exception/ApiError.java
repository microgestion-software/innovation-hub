// src/main/java/com/microgestion/example/patterns/ratelimiting/api/exception/ApiError.java
package com.microgestion.example.patterns.resilience.api.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard error response structure")
public class ApiError {

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "HTTP status description", example = "Bad Request")
    private String error;

    @Schema(description = "Main error message", example = "Validation failed for the request")
    private String message;

    @Schema(description = "Path of the request that generated the error", example = "/api/customers/1")
    private String path;

    @Schema(description = "Unique error reference code", example = "ERR-001")
    private String errorReference;

    @Schema(description = "Timestamp when the error occurred")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime timestamp;

    @Schema(description = "Detailed error messages or field-specific validation errors")
    @Builder.Default
    private List<ValidationError> details = new ArrayList<>();

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "Validation error details for specific fields")
    public static class ValidationError {
        @Schema(description = "Name of the field with error", example = "email")
        private String field;

        @Schema(description = "Error message for the field", example = "Email format is invalid")
        private String message;

        @Schema(description = "Rejected value", example = "invalid-email")
        private Object rejectedValue;
    }

    /**
     * Static factory method for creating basic error responses
     */
    public static ApiError of(int status, String error, String message, String path) {
        return ApiError.builder()
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .errorReference(generateErrorReference())
                .build();
    }

    /**
     * Static factory method for creating validation error responses
     */
    public static ApiError ofValidationError(String message, String path, List<ValidationError> details) {
        return ApiError.builder()
                .status(400)
                .error("Bad Request")
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .errorReference(generateErrorReference())
                .details(details)
                .build();
    }

    /**
     * Static factory method for rate limit exceeded errors
     */
    public static ApiError ofRateLimitExceeded(String path) {
        return ApiError.builder()
                .status(429)
                .error("Too Many Requests")
                .message("Rate limit exceeded. Please try again later.")
                .path(path)
                .timestamp(LocalDateTime.now())
                .errorReference(generateErrorReference())
                .build();
    }

    /**
     * Static factory method for resource not found errors
     */
    public static ApiError ofNotFound(String resource, String id, String path) {
        return ApiError.builder()
                .status(404)
                .error("Not Found")
                .message(String.format("%s with id %s not found", resource, id))
                .path(path)
                .timestamp(LocalDateTime.now())
                .errorReference(generateErrorReference())
                .build();
    }

    /**
     * Generates a unique error reference code
     */
    private static String generateErrorReference() {
        return "ERR-" + System.currentTimeMillis() % 100000;
    }
}