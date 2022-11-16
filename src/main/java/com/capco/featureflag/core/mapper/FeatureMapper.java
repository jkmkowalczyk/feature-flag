package com.capco.featureflag.core.mapper;

import com.capco.featureflag.core.dto.CreateFeatureRequest;
import com.capco.featureflag.core.dto.UserFeaturesResponse;
import com.capco.featureflag.core.entity.FeatureEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface FeatureMapper {
    @Mapping(target = "isEnabledGlobally", constant = "false")
    FeatureEntity toEntity(CreateFeatureRequest request);

    default Set<UserFeaturesResponse.FeatureResponse> mapToResponse(Set<FeatureEntity> set) {
        return set.stream().map(this::toResponse).collect(Collectors.toSet());
    }

    UserFeaturesResponse.FeatureResponse toResponse(FeatureEntity entity);
}
