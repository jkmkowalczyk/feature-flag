package com.capco.featureflag.core.exception;

public class FeatureNotFoundException extends ResourceNotFoundException {

    public FeatureNotFoundException(Long id) {
        super(String.format("Feature with id: %d not found", id));
    }
}
