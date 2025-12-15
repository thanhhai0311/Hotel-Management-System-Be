package com.javaweb.service;

import com.javaweb.model.dto.BookingServiceDTO.BookingServiceBulkRequest;
import com.javaweb.model.dto.BookingServiceDTO.BookingServiceDTO;

import java.util.List;


public interface BookingServiceService {
    List<BookingServiceDTO> addServicesToRoom(BookingServiceBulkRequest request);

    void updateServiceQuantity(Integer id, Integer quantity);

    void deleteService(Integer id);

    List<BookingServiceDTO> getServicesByBookingRoom(Integer bookingRoomId);

    void cancelService(Integer id);

    List<BookingServiceDTO> getAllBookingServices(Integer page, Integer limit);

    BookingServiceDTO getBookingServiceById(Integer id);
}
