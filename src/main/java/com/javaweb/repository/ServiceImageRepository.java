package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaweb.model.entity.ServiceImageEntity;

@Repository
public interface ServiceImageRepository extends JpaRepository<ServiceImageEntity, Integer> {
}
