package com.javaweb.controller;

import com.javaweb.model.dto.BookingServiceDTO.BookingServiceBulkRequest;
import com.javaweb.model.dto.BookingServiceDTO.BookingServiceDTO;
import com.javaweb.service.BookingServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking-services")
public class BookingServiceController {

    @Autowired
    private BookingServiceService bookingServiceService;

    @PostMapping("/add")
    public ResponseEntity<?> addServices(@RequestBody BookingServiceBulkRequest request) {
        if (request.getBookingRoomId() == null || request.getItems() == null || request.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body("Dữ liệu không hợp lệ!");
        }

        List<BookingServiceDTO> result = bookingServiceService.addServicesToRoom(request);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-quantity/{id}")
    public ResponseEntity<?> updateQuantity(@PathVariable Integer id, @RequestParam Integer quantity) {
        bookingServiceService.updateServiceQuantity(id, quantity);
        return ResponseEntity.ok("Cập nhật thành công!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteService(@PathVariable Integer id) {
        bookingServiceService.deleteService(id);
        return ResponseEntity.ok("Xóa thành công!");
    }

    @GetMapping("/booking-room/{id}")
    public ResponseEntity<List<BookingServiceDTO>> getServicesByRoom(@PathVariable Integer id) {
        List<BookingServiceDTO> list = bookingServiceService.getServicesByBookingRoom(id);
        return ResponseEntity.ok(list);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelService(@PathVariable Integer id) {
        bookingServiceService.cancelService(id);
        return ResponseEntity.ok("Đã hủy dịch vụ thành công!");
    }

    @GetMapping
    public ResponseEntity<List<BookingServiceDTO>> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit
    ) {
        List<BookingServiceDTO> result = bookingServiceService.getAllBookingServices(page, limit);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingServiceDTO> getById(@PathVariable Integer id) {
        BookingServiceDTO result = bookingServiceService.getBookingServiceById(id);
        return ResponseEntity.ok(result);
    }
}