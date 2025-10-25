package com.javaweb.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.javaweb.model.entity.RoomTypeEntity;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomTypeEntity, Integer> {
    boolean existsByNameIgnoreCase(String name);
    Optional<RoomTypeEntity> findByNameIgnoreCase(String name);
}
