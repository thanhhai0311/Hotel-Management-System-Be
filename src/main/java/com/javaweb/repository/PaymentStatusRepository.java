package com.javaweb.repository;

import com.javaweb.model.entity.PaymentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatusEntity, Integer> {
}
