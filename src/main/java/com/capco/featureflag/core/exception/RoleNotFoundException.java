package com.capco.featureflag.core.exception;

public class RoleNotFoundException extends ResourceNotFoundException {

    public RoleNotFoundException(String name) {
        super(String.format("Role with name: %s not found", name));
    }
}
