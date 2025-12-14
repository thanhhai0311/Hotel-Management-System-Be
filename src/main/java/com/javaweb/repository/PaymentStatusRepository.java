package com.javaweb.repository;

import com.javaweb.model.entity.PaymentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatusEntity, Integer> {
    Optional<PaymentStatusEntity> findByName(String name);
}
