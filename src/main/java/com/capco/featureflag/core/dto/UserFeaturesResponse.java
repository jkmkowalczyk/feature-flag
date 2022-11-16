package com.capco.featureflag.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFeaturesResponse {
    private Set<FeatureResponse> enabledGlobally;
    private Set<FeatureResponse> enabledForUser;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FeatureResponse {
        private String name;
        private String description;
    }
}
