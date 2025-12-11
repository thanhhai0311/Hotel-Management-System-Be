package com.javaweb.model.dto.BookingRoomDTO;

import java.time.LocalDate;

public class BookingItemDTO {
    private Integer roomId;
    private Integer roomPromotionId; // Có thể null

    // Chỉ nhận ngày (yyyy-MM-dd)
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    // Getters & Setters
    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getRoomPromotionId() {
        return roomPromotionId;
    }

    public void setRoomPromotionId(Integer roomPromotionId) {
        this.roomPromotionId = roomPromotionId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
}