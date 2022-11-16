package com.capco.featureflag.core.exception.handler.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class ErrorResponseWithViolations extends ErrorResponse {
    private final List<String> violations;
}
