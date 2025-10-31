package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.javaweb.model.entity.TypeImageEntity;

@Repository
public interface TypeImageRepository extends JpaRepository<TypeImageEntity, Integer> {
}
