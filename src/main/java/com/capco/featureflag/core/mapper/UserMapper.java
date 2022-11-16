package com.capco.featureflag.core.mapper;

import com.capco.featureflag.core.dto.CreateUserRequest;
import com.capco.featureflag.core.entity.UserEntity;
import com.capco.featureflag.core.exception.RoleNotFoundException;
import com.capco.featureflag.core.repository.RoleRepository;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

import static com.capco.featureflag.configuration.SecurityConfig.USER_ROLE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
@DecoratedWith(UserMapper.UserMapperDecorator.class)
public interface UserMapper {
    default UserEntity toEntity(CreateUserRequest request) {
        return null;
    }

    abstract class UserMapperDecorator implements UserMapper {

        @Autowired
        private PasswordEncoder passwordEncoder;
        @Autowired
        private RoleRepository roleRepository;

        @Override
        public UserEntity toEntity(CreateUserRequest request) {
            return UserEntity.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .roles(Collections.singleton(roleRepository.findByName(USER_ROLE).orElseThrow(() -> new RoleNotFoundException(USER_ROLE))))
                    .build();
        }
    }
}
