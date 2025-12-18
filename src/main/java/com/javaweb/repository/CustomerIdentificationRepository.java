package com.javaweb.repository;

import com.javaweb.model.entity.CustomerIdentificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerIdentificationRepository extends JpaRepository<CustomerIdentificationEntity, Integer> {
    Optional<CustomerIdentificationEntity> findByCustomer_Id(Integer userId);

    List<CustomerIdentificationEntity> findByExpiryDateBefore(LocalDateTime now);
}