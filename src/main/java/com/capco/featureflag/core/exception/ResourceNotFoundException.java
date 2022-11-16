package com.capco.featureflag.core.exception;

import org.springframework.http.HttpStatus;

public abstract class ResourceNotFoundException extends ServiceException {

    protected ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND", message);
    }
}
