package com.javaweb.repository;

import com.javaweb.model.entity.BookingRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface BookingRoomRepository extends JpaRepository<BookingRoomEntity, Integer> {

    // Logic: Kiểm tra giao nhau giữa 2 khoảng ngày
    // (StartA < EndB) && (EndA > StartB)
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM BookingRoomEntity b " +
            "WHERE b.room.id = :roomId " +
            "AND b.contractCheckInTime < :checkOut " +
            "AND b.contractCheckOutTime > :checkIn")
    boolean existsByRoomIdAndOverlappingTime(@Param("roomId") Integer roomId,
                                             @Param("checkIn") LocalDateTime checkIn,
                                             @Param("checkOut") LocalDateTime checkOut);
}