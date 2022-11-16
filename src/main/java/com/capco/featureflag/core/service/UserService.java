package com.capco.featureflag.core.service;

import com.capco.featureflag.core.dto.CreateUserRequest;
import com.capco.featureflag.core.dto.UserFeaturesResponse;
import com.capco.featureflag.core.entity.UserEntity;
import com.capco.featureflag.core.exception.RoleNotFoundException;
import com.capco.featureflag.core.exception.UserAlreadyExistsException;
import com.capco.featureflag.core.exception.UserNotFoundException;
import com.capco.featureflag.core.mapper.UserMapper;
import com.capco.featureflag.core.repository.RoleRepository;
import com.capco.featureflag.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.capco.featureflag.configuration.SecurityConfig.ADMIN_ROLE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    private final UserMapper mapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FeatureService featureService;

    public Long createUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(request.getUsername());
        }
        UserEntity user = userRepository.save(mapper.toEntity(request));
        return user.getId();
    }

    public Long createAdminUser(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(request.getUsername());
        }
        UserEntity user = mapper.toEntity(request);
        user.addRole(roleRepository.findByName(ADMIN_ROLE).orElseThrow(() -> new RoleNotFoundException(ADMIN_ROLE)));
        user = userRepository.save(user);
        return user.getId();
    }

    public void enableFeatureForUser(Long userId, Long featureId) {
        getUser(userId).enableFeature(featureService.getFeature(featureId));
    }

    public UserFeaturesResponse getFeaturesForUser(Long userId) {
        return getFeaturesForUser(getUser(userId));
    }

    public UserFeaturesResponse getFeaturesForUser(String username) {
        return getFeaturesForUser(getUser(username));
    }

    private UserEntity getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    private UserEntity getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    private UserFeaturesResponse getFeaturesForUser(UserEntity user) {
        return featureService.getEnabledFeatures(user.getEnabledFeatures());
    }

}
