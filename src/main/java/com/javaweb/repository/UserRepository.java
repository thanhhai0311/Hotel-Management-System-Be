package com.javaweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.javaweb.model.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer>{
	UserEntity findById(String id); 
	
	Optional<UserEntity> findByAccount_Email(String email);
	
	@Query("SELECT u FROM UserEntity u WHERE u.account.role.id = :roleId")
    List<UserEntity> findByRoleId(Integer roleId);

    @Query("SELECT u FROM UserEntity u WHERE u.account.role.id = :roleId")
    Page<UserEntity> findByRoleId(Integer roleId, Pageable pageable);
}
