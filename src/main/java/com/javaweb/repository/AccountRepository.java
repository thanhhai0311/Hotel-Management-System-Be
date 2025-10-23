package com.javaweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaweb.model.entity.AccountEntity;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    Optional<AccountEntity> findByEmail(String email);
    Optional<AccountEntity> findById(Integer id);
    boolean existsByEmail(String email);
    List<AccountEntity> findAll();
    boolean existsById(Integer id);
	void deleteById(Integer id);
}
