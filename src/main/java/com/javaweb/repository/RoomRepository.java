package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.javaweb.model.entity.RoomEntity;

public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {
}
