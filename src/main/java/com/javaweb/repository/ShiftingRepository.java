package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.javaweb.model.entity.ShiftingEntity;

public interface ShiftingRepository extends JpaRepository<ShiftingEntity, Integer>, JpaSpecificationExecutor<ShiftingEntity> {
}
