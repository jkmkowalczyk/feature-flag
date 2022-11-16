package com.capco.featureflag.core.exception;

public class FeatureAlreadyExistsException extends ResourceAlreadyExistsException {

    public FeatureAlreadyExistsException(String name) {
        super(String.format("Feature with name: %s already exists", name));
    }
}
