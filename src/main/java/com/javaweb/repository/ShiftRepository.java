package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.javaweb.model.entity.ShiftEntity;

@Repository
public interface ShiftRepository extends JpaRepository<ShiftEntity, Integer>, JpaSpecificationExecutor<ShiftEntity> {
}
