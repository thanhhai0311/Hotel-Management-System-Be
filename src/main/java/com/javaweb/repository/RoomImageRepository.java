package com.javaweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.javaweb.model.entity.RoomImageEntity;

public interface RoomImageRepository extends JpaRepository<RoomImageEntity, Integer> {
	List<RoomImageEntity> findByRoomId(Integer roomId);
	Optional<RoomImageEntity> findBySrc(String src);
}
