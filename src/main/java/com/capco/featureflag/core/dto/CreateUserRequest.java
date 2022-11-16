package com.capco.featureflag.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    @NotEmpty(message = "must not be null nor empty")
    private String username;
    @NotEmpty(message = "must not be null nor empty")
    private String password;
}
