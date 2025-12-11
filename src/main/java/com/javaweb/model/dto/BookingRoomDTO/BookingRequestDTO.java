package com.javaweb.model.dto.BookingRoomDTO;

import java.util.List;

public class BookingRequestDTO {
    private Integer customerId;
    private List<BookingItemDTO> bookings;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<BookingItemDTO> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingItemDTO> bookings) {
        this.bookings = bookings;
    }
}
