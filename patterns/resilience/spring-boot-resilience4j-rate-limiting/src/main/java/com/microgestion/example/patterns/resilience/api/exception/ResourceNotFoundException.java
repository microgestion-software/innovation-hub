package com.microgestion.example.patterns.resilience.api.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final String resourceName;
    private final String identifier;

    public ResourceNotFoundException(String resourceName, String identifier) {
        super(String.format("%s with identifier %s not found", resourceName, identifier));
        this.resourceName = resourceName;
        this.identifier = identifier;
    }
}