package com.javaweb.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.javaweb.model.entity.RoomStatusEntity;

@Repository
public interface RoomStatusRepository extends JpaRepository<RoomStatusEntity, Integer> {
    boolean existsByNameIgnoreCase(String name);
    Optional<RoomStatusEntity> findByNameIgnoreCase(String name);
}
