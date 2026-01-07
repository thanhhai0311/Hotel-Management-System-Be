package com.javaweb.repository;

import com.javaweb.model.entity.BlackListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListRepository extends JpaRepository<BlackListEntity, Integer> {
    Optional<BlackListEntity> findByCustomerId(Integer customerId);
}
