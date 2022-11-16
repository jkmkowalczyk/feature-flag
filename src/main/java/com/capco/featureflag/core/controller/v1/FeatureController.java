package com.capco.featureflag.core.controller.v1;

import com.capco.featureflag.core.dto.CreateFeatureRequest;
import com.capco.featureflag.core.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/v1/features")
public class FeatureController {
    private final FeatureService service;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Long> createFeature(@Valid @RequestBody CreateFeatureRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createFeature(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/enable/{featureId}")
    public ResponseEntity<Object> enableFeatureGlobally(@PathVariable Long featureId) {
        service.enableFeatureGlobally(featureId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
