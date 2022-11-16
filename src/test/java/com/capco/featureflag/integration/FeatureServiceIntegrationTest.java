package com.capco.featureflag.integration;

import com.capco.featureflag.core.dto.CreateFeatureRequest;
import com.capco.featureflag.core.entity.FeatureEntity;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class FeatureServiceIntegrationTest extends IntegrationTestBase {

    @Test
    void createFeature_validData_createsFeatureWithDisabledFlag() {
        createAdminIfNotExists();
        setupAuthSchemeForAdminUser();
        ValidatableResponse response = given()
                .body(CreateFeatureRequest.builder()
                        .name("name")
                        .description("description")
                        .build())
                .post("/v1/features/create").then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.CREATED.value());
        Long featureId = response.extract().response().getBody().as(Long.class);
        Optional<FeatureEntity> optional = featureRepository.findById(featureId);
        assertThat(optional).isPresent();
        FeatureEntity feature = optional.get();
        assertThat(feature.getName()).isEqualTo("name");
        assertThat(feature.getDescription()).isEqualTo("description");
        assertThat(feature.getIsEnabledGlobally()).isFalse();
    }

    @Test
    void enablesFeature_validData_enablesFeatureGlobally() {
        Long featureId = createFeature(UUID.randomUUID().toString());
        setupAuthSchemeForAdminUser();
        given()
                .put("/v1/features/enable/" + featureId).then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.OK.value());
        Optional<FeatureEntity> optional = featureRepository.findById(featureId);
        assertThat(optional).isPresent();
        FeatureEntity feature = optional.get();
        assertThat(feature.getIsEnabledGlobally()).isTrue();
    }

    @Test
    void createFeature_missingRole_responseCodeUnauthorized() {
        String username = UUID.randomUUID().toString();
        createTestUser(username);
        setupAuthSchemeForTestUser(username);
        given()
                .body(CreateFeatureRequest.builder()
                        .name("name")
                        .description("description")
                        .build())
                .post("/v1/features/create").then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

}