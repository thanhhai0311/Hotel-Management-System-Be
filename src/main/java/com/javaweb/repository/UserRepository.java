package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javaweb.model.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer>{

}
