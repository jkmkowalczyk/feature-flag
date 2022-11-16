package com.capco.featureflag.integration;

import com.capco.featureflag.core.dto.CreateFeatureRequest;
import com.capco.featureflag.core.dto.CreateUserRequest;
import com.capco.featureflag.core.dto.UserFeaturesResponse;
import com.capco.featureflag.core.entity.UserEntity;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.capco.featureflag.configuration.SecurityConfig.USER_ROLE;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

class UserServiceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    void registerUser_validData_createsUserWithUserRoleAndEncryptedPassword() {
        given()
                .body(CreateUserRequest.builder()
                        .username("username")
                        .password("password")
                        .build())
                .post("/v1/users/register").then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.CREATED.value());
        Optional<UserEntity> optional = userRepository.findByUsername("username");
        assertThat(optional).isPresent();
        UserEntity user = optional.get();
        assertThat(user.getUsername()).isEqualTo("username");
        assertThat(passwordEncoder.matches("password", user.getPassword())).isTrue();
        assertThat(user.getRoles()).hasSize(1);
        assertThat(user.getRoles()).contains(USER_ROLE);
    }

    @Test
    void registerUser_invalidData_returnsBadRequestCode() {
        given()
                .body(new CreateUserRequest())
                .post("/v1/users/register").then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void registerUser_repeatedUsername_returnsBadRequestCode() {
        String username = UUID.randomUUID().toString();
        createTestUser(username);
        given()
                .body(CreateUserRequest.builder()
                        .username(username)
                        .password("password")
                        .build())
                .post("/v1/users/register").then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getUsersFeatures_userWithoutFeatures_returnsEmptyUsersFeatures() {
        String username = UUID.randomUUID().toString();
        createTestUser(username);
        setupAuthSchemeForTestUser(username);

        ValidatableResponse response = given()
                .get("/v1/users/features")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.OK.value());
        UserFeaturesResponse featuresResponse = response.extract().response().getBody().as(UserFeaturesResponse.class);
        assertThat(featuresResponse.getEnabledForUser()).isEmpty();
        assertThat(featuresResponse.getEnabledGlobally()).hasSize(featureRepository.findAllByIsEnabledGloballyTrue().size());
    }

    @Test
    void getUsersFeatures_userWithFeatures_returnsUsersFeatures() {
        String username = UUID.randomUUID().toString();
        Long userId = createTestUser(username);
        Long featureId = featureService.createFeature(CreateFeatureRequest.builder().name("feature").build());
        featureService.enableFeatureGlobally(featureId);
        userService.enableFeatureForUser(userId, featureId);
        setupAuthSchemeForTestUser(username);
        ValidatableResponse response = given()
                .get("/v1/users/features")
                .then()
                .log().ifValidationFails()
                .statusCode(HttpStatus.OK.value());
        UserFeaturesResponse featuresResponse = response.extract().response().getBody().as(UserFeaturesResponse.class);
        assertThat(featuresResponse.getEnabledForUser()).hasSize(1);
        assertThat(featuresResponse.getEnabledGlobally()).hasSize(featureRepository.findAllByIsEnabledGloballyTrue().size());
    }

}