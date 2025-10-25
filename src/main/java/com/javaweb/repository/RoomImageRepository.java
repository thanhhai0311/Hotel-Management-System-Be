package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.javaweb.model.entity.RoomImageEntity;

public interface RoomImageRepository extends JpaRepository<RoomImageEntity, Integer> {
}
