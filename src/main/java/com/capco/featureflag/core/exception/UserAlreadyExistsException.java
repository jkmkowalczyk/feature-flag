package com.capco.featureflag.core.exception;

public class UserAlreadyExistsException extends ResourceAlreadyExistsException {

    public UserAlreadyExistsException(String username) {
        super(String.format("User with username: %s already exists", username));
    }
}
