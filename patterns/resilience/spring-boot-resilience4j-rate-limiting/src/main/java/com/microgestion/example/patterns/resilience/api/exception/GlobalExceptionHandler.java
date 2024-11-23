package com.microgestion.example.patterns.resilience.api.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // @ExceptionHandler(RequestNotPermitted.class)
        // public Mono<ResponseEntity<ApiError>> handleRateLimitExceeded(
        // RequestNotPermitted ex,
        // ServerWebExchange exchange) {
        // ApiError error = ApiError.ofRateLimitExceeded(
        // exchange.getRequest().getPath().toString());
        // return
        // Mono.just(ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(error));
        // }

        @ExceptionHandler(RequestNotPermitted.class)
        public Mono<ResponseEntity<ApiError>> handleRateLimitExceeded(
                        RequestNotPermitted ex,
                        ServerWebExchange exchange) {

                ApiError error = ApiError.builder()
                                .status(HttpStatus.TOO_MANY_REQUESTS.value())
                                .error("Too Many Requests")
                                .message("Rate limit exceeded. Please try again later.")
                                .path(exchange.getRequest().getPath().value())
                                .timestamp(LocalDateTime.now())
                                .build();

                return Mono.just(ResponseEntity
                                .status(HttpStatus.TOO_MANY_REQUESTS)
                                .header("Retry-After", "1") // Suggest retry after 1 second
                                .body(error));
        }

        @ExceptionHandler(WebExchangeBindException.class)
        public Mono<ResponseEntity<ApiError>> handleValidationErrors(
                        WebExchangeBindException ex,
                        ServerWebExchange exchange) {
                List<ApiError.ValidationError> validationErrors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(fieldError -> ApiError.ValidationError.builder()
                                                .field(fieldError.getField())
                                                .message(fieldError.getDefaultMessage())
                                                .rejectedValue(fieldError.getRejectedValue())
                                                .build())
                                .collect(Collectors.toList());

                ApiError error = ApiError.ofValidationError(
                                "Validation failed",
                                exchange.getRequest().getPath().toString(),
                                validationErrors);
                return Mono.just(ResponseEntity.badRequest().body(error));
        }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public Mono<ResponseEntity<ApiError>> handleDataIntegrityViolation(
                        DataIntegrityViolationException ex,
                        ServerWebExchange exchange) {
                ApiError error = ApiError.of(
                                HttpStatus.CONFLICT.value(),
                                "Conflict",
                                "Data integrity violation: " + ex.getMostSpecificCause().getMessage(),
                                exchange.getRequest().getPath().toString());
                return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error));
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public Mono<ResponseEntity<ApiError>> handleResourceNotFound(
                        ResourceNotFoundException ex,
                        ServerWebExchange exchange) {
                ApiError error = ApiError.ofNotFound(
                                ex.getResourceName(),
                                ex.getIdentifier(),
                                exchange.getRequest().getPath().toString());
                return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
        }

        @ExceptionHandler(Exception.class)
        public Mono<ResponseEntity<ApiError>> handleGenericError(
                        Exception ex,
                        ServerWebExchange exchange) {
                ApiError error = ApiError.of(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                "An unexpected error occurred",
                                exchange.getRequest().getPath().toString());
                ex.printStackTrace();
                return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
        }
}