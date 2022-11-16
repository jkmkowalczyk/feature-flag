package com.capco.featureflag.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feature")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class FeatureEntity extends BaseEntity {

    @EqualsAndHashCode.Include
    private String name;

    @EqualsAndHashCode.Include
    private String description;

    private Boolean isEnabledGlobally;

    @ManyToMany(mappedBy = "enabledFeatures", fetch = FetchType.LAZY)
    private Set<UserEntity> enabledUsers;

    public void enableGlobally() {
        this.isEnabledGlobally = true;
    }
}
