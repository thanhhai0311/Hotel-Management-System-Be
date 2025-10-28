package com.javaweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaweb.model.entity.ServiceCategoryEntity;

@Repository
public interface ServiceCategoryRepository extends JpaRepository<ServiceCategoryEntity, Integer> {
	 boolean existsByNameIgnoreCase(String name);
	 Optional<ServiceCategoryEntity> findByNameIgnoreCase(String name);
}
