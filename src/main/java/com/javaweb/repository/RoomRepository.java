package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.javaweb.model.entity.RoomEntity;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Integer>, JpaSpecificationExecutor<RoomEntity> {
    boolean existsByRoomNumberAndHotel_Id(int roomNumber, Integer hotelId);
}
