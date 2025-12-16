package com.javaweb.repository;

import com.javaweb.model.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Integer>, JpaSpecificationExecutor<RoomEntity> {
    boolean existsByRoomNumberAndHotel_Id(int roomNumber, Integer hotelId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM RoomEntity r WHERE r.id = :id")
    Optional<RoomEntity> findByIdWithLock(@Param("id") Integer id);

    @Query("SELECT r FROM RoomEntity r " +
            "WHERE r.roomStatus.id = 1 " +
            "AND r.id NOT IN (" +
            "   SELECT b.room.id FROM BookingRoomEntity b " +
            "   WHERE b.status = 1 " +
            "   AND (" +
            "       (b.contractCheckInTime < :checkOutTime) AND " +
            "       (b.contractCheckOutTime > :checkInTime)" +
            "   )" +
            ")")
    List<RoomEntity> findAvailableRooms(
            @Param("checkInTime") LocalDateTime checkInTime,
            @Param("checkOutTime") LocalDateTime checkOutTime
    );


}
