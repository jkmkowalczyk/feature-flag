package com.capco.featureflag.core.exception;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(Long id) {
        super(String.format("User with id: %d not found", id));
    }

    public UserNotFoundException(String username) {
        super(String.format("User with username: %s not found", username));
    }
}
