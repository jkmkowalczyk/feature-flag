package com.capco.featureflag.core.controller.v1;

import com.capco.featureflag.core.dto.CreateUserRequest;
import com.capco.featureflag.core.dto.UserFeaturesResponse;
import com.capco.featureflag.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
@Validated
public class UserController {
    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<Long> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}/features")
    public ResponseEntity<UserFeaturesResponse> getFeaturesForUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getFeaturesForUser(userId));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/features")
    public ResponseEntity<UserFeaturesResponse> getFeaturesForUser(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body(service.getFeaturesForUser(authentication.getName()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{userId}/enable/{featureId}")
    public ResponseEntity<Object> enableFeatureForUser(@PathVariable @Positive @NotNull Long userId, @PathVariable @Positive @NotNull Long featureId) {
        service.enableFeatureForUser(userId, featureId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
