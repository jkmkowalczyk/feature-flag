package com.capco.featureflag.core.exception.handler.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ErrorResponse {
    private final String code;
    private final String message;
}
