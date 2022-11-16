package com.capco.featureflag.core.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class UserEntity extends BaseEntity {

    @EqualsAndHashCode.Include
    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_feature",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id"))
    private Set<FeatureEntity> enabledFeatures = new HashSet<>();

    public Set<FeatureEntity> getEnabledFeatures() {
        return Collections.unmodifiableSet(enabledFeatures);
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    public Set<String> getRoles() {
        return Optional.ofNullable(roles).orElse(new HashSet<>()).stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toUnmodifiableSet());
    }

    public void enableFeature(FeatureEntity feature) {
        if (Objects.isNull(enabledFeatures)) {
            enabledFeatures = new HashSet<>();
        }
        enabledFeatures.add(feature);
    }

    public void addRole(RoleEntity role) {
        if (Objects.isNull(roles)) {
            roles = new HashSet<>();
        }
        roles.add(role);
    }

}
