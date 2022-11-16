package com.capco.featureflag.core.repository;

import com.capco.featureflag.core.entity.FeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
@Transactional
public interface FeatureRepository extends JpaRepository<FeatureEntity, Long> {
    Set<FeatureEntity> findAllByIsEnabledGloballyTrue();

    boolean existsByName(String name);
}
