package com.capco.featureflag.core.exception.handler;

import com.capco.featureflag.core.exception.ServiceException;
import com.capco.featureflag.core.exception.handler.dto.ErrorResponse;
import com.capco.featureflag.core.exception.handler.dto.ErrorResponseWithViolations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Validation exception occurred", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponseWithViolations
                        .builder()
                        .code("BAD_REQUEST")
                        .message("Constraint violation")
                        .violations(e.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(v -> String.format("field %s: %s", v.getField(), v.getDefaultMessage()))
                                .sorted()
                                .toList())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("Access denied exception occurred", e);
        return newResponseEntity(HttpStatus.UNAUTHORIZED, "MISSING_ROLE", e.getMessage());
    }

    @ExceptionHandler(ServiceException.class)
    ResponseEntity<ErrorResponse> handleServiceException(ServiceException e) {
        log.error("Service exception occurred", e);
        return newResponseEntity(e.getHttpStatus(), e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Unknown exception occurred", e);
        return newResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_ERROR", e.getMessage());
    }

    private ResponseEntity<ErrorResponse> newResponseEntity(HttpStatus httpStatus, String code, String message) {
        return ResponseEntity
                .status(httpStatus)
                .body(ErrorResponse
                        .builder()
                        .code(code)
                        .message(message)
                        .build());
    }

}
