package com.capco.featureflag.integration;

import com.capco.featureflag.core.dto.CreateUserRequest;
import com.capco.featureflag.core.exception.UserAlreadyExistsException;
import com.capco.featureflag.core.exception.UserNotFoundException;
import com.capco.featureflag.core.repository.UserRepository;
import com.capco.featureflag.core.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    UserService service;
    @Mock
    UserRepository repository;

    @Test
    void createFeature_nameAlreadyExists_throwsException() {
        Mockito.when(repository.existsByUsername("username")).thenReturn(true);
        CreateUserRequest request = CreateUserRequest.builder().username("username").password("password").build();
        UserAlreadyExistsException thrown = Assertions.assertThrows(UserAlreadyExistsException.class, () ->
                service.createUser(request));
        assertThat(thrown.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void enableFeatureForUser_userNotExists_throwsException() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());
        UserNotFoundException thrown = Assertions.assertThrows(UserNotFoundException.class, () ->
                service.enableFeatureForUser(1L, 1L));
        assertThat(thrown.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}