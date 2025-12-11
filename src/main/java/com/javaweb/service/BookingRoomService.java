package com.javaweb.service;

import com.javaweb.model.dto.BookingRoomDTO.BookingRequestDTO;

import java.util.Map;

public interface BookingRoomService {
    Map<String, Object> createBooking(BookingRequestDTO request);
}
