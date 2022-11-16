package com.capco.featureflag.integration;

import com.capco.featureflag.core.dto.CreateFeatureRequest;
import com.capco.featureflag.core.dto.CreateUserRequest;
import com.capco.featureflag.core.entity.BaseEntity;
import com.capco.featureflag.core.repository.FeatureRepository;
import com.capco.featureflag.core.repository.RoleRepository;
import com.capco.featureflag.core.repository.UserRepository;
import com.capco.featureflag.core.service.FeatureService;
import com.capco.featureflag.core.service.UserService;
import io.restassured.RestAssured;
import io.restassured.authentication.PreemptiveBasicAuthScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTestBase {

    @Autowired
    UserRepository userRepository;
    @Autowired
    FeatureService featureService;
    @Autowired
    FeatureRepository featureRepository;
    @Autowired
    UserService userService;
    @Autowired
    RoleRepository roleRepository;

    @BeforeAll
    static void beforeAll(@LocalServerPort int localPort) {
        // Set up RestAssured
        RestAssured.port = localPort;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    Long createTestUser(String username) {
        return userService.createUser(CreateUserRequest.builder()
                .username(username)
                .password("password")
                .build());
    }

    void createAdminIfNotExists() {
        userRepository.findByUsername("admin")
                .map(BaseEntity::getId)
                .orElseGet(() -> userService.createAdminUser(CreateUserRequest.builder()
                        .username("admin")
                        .password("admin")
                        .build()));
    }

    Long createFeature(String name) {
        return featureService.createFeature(CreateFeatureRequest.builder()
                .name(name)
                .description("description")
                .build());
    }

    void setupAuthSchemeForTestUser(String username) {
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName(username);
        authScheme.setPassword("password");
        RestAssured.authentication = authScheme;
    }

    void setupAuthSchemeForAdminUser() {
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
        authScheme.setUserName("admin");
        authScheme.setPassword("admin");
        RestAssured.authentication = authScheme;
    }

}
