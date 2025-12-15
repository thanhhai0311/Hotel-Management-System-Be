package com.javaweb.repository;

import com.javaweb.model.entity.BookingServiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingServiceRepository extends JpaRepository<BookingServiceEntity, Integer> {
    List<BookingServiceEntity> findByBookingRoom_Id(Integer bookingRoomId);

    Page<BookingServiceEntity> findByBookingRoom_Customer_Id(Integer customerId, Pageable pageable);

    List<BookingServiceEntity> findByBookingRoom_Customer_Id(Integer customerId, Sort sort);
}
