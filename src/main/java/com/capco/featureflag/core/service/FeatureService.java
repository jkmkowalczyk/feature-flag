package com.capco.featureflag.core.service;

import com.capco.featureflag.core.dto.CreateFeatureRequest;
import com.capco.featureflag.core.dto.UserFeaturesResponse;
import com.capco.featureflag.core.entity.FeatureEntity;
import com.capco.featureflag.core.exception.FeatureAlreadyExistsException;
import com.capco.featureflag.core.exception.FeatureNotFoundException;
import com.capco.featureflag.core.mapper.FeatureMapper;
import com.capco.featureflag.core.repository.FeatureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FeatureService {
    private final FeatureMapper featureMapper;
    private final FeatureRepository featureRepository;

    public Long createFeature(CreateFeatureRequest request) {
        if (featureRepository.existsByName(request.getName())) {
            throw new FeatureAlreadyExistsException(request.getName());
        }
        FeatureEntity feature = featureRepository.save(featureMapper.toEntity(request));
        return feature.getId();
    }

    public void enableFeatureGlobally(Long featureId) {
        getFeature(featureId).enableGlobally();
    }

    UserFeaturesResponse getEnabledFeatures(Set<FeatureEntity> userEnabledFeatures) {
        return UserFeaturesResponse.builder()
                .enabledGlobally(mapEnabledFeatures(featureRepository.findAllByIsEnabledGloballyTrue()))
                .enabledForUser(mapEnabledFeatures(userEnabledFeatures))
                .build();
    }

    FeatureEntity getFeature(Long featureId) {
        return featureRepository.findById(featureId).orElseThrow(() -> new FeatureNotFoundException(featureId));
    }

    private Set<UserFeaturesResponse.FeatureResponse> mapEnabledFeatures(Set<FeatureEntity> enabledFeatures) {
        return featureMapper.mapToResponse(enabledFeatures);
    }
}
