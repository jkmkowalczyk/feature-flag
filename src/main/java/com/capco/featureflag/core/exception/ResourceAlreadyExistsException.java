package com.capco.featureflag.core.exception;

import org.springframework.http.HttpStatus;

public abstract class ResourceAlreadyExistsException extends ServiceException {

    protected ResourceAlreadyExistsException(String message) {
        super(HttpStatus.BAD_REQUEST, "RESOURCE_ALREADY_EXISTS", message);
    }
}
