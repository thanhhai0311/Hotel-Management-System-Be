package com.javaweb.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.model.entity.CustomerEntity;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByEmail(String email);
}
