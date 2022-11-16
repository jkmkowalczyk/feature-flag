package com.capco.featureflag.integration;

import com.capco.featureflag.core.dto.CreateFeatureRequest;
import com.capco.featureflag.core.exception.FeatureAlreadyExistsException;
import com.capco.featureflag.core.exception.FeatureNotFoundException;
import com.capco.featureflag.core.repository.FeatureRepository;
import com.capco.featureflag.core.service.FeatureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class FeatureServiceTest {
    @InjectMocks
    FeatureService service;
    @Mock
    FeatureRepository repository;

    @Test
    void createUser_usernameAlreadyExists_throwsException() {
        Mockito.when(repository.existsByName("name")).thenReturn(true);
        CreateFeatureRequest request = CreateFeatureRequest.builder().name("name").build();
        FeatureAlreadyExistsException thrown = Assertions.assertThrows(FeatureAlreadyExistsException.class, () ->
                service.createFeature(request));
        assertThat(thrown.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void enableFeature_featureNotExists_throwsException() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());
        FeatureNotFoundException thrown = Assertions.assertThrows(FeatureNotFoundException.class, () ->
                service.enableFeatureGlobally(1L));
        assertThat(thrown.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}