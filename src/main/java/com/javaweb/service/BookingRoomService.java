package com.javaweb.service;

import com.javaweb.model.dto.BookingRoomDTO.BookingRequestDTO;
import com.javaweb.model.dto.BookingRoomDTO.BookingResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BookingRoomService {
    Map<String, Object> createBooking(BookingRequestDTO request);

    List<BookingResponseDTO> getAllBookings(Integer page, Integer limit);

    BookingResponseDTO getBookingById(Integer id);

    void cancelBooking(Integer id);

    void checkIn(Integer id, MultipartFile cccdImage);

    void checkOut(Integer id);
}
