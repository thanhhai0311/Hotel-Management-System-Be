package com.javaweb.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.javaweb.model.entity.RoomTypeEntity;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomTypeEntity, Integer>, JpaSpecificationExecutor<RoomTypeEntity> {
	boolean existsByName(String name);
	
	List<RoomTypeEntity> findByIsDeletedFalse();

    Page<RoomTypeEntity> findByIsDeletedFalse(Pageable pageable);
   
}
