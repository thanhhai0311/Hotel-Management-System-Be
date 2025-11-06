package com.javaweb.repository;

import com.javaweb.model.entity.ReviewImageEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewImageRepository extends JpaRepository<ReviewImageEntity, Integer> {
	Optional<ReviewImageEntity> findBySrc(String src);
}
