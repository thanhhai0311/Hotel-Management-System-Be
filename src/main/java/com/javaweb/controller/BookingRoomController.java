package com.javaweb.controller;

import com.javaweb.model.dto.BookingRoomDTO.BookingRequestDTO;
import com.javaweb.model.dto.BookingRoomDTO.BookingResponseDTO;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.BookingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/booking")
public class BookingRoomController {

    @Autowired
    private BookingRoomService bookingRoomService;

    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDTO request) {
        try {
            // Gọi Service để xử lý logic (bao gồm validate, lock, tạo bill)
            Map<String, Object> result = bookingRoomService.createBooking(request);

            // Chuẩn hóa phản hồi thành công (HTTP 201 Created)
            ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                    true,
                    HttpStatus.CREATED.value(),
                    "Đặt phòng thành công",
                    result,
                    "api/booking/create"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (ResponseStatusException e) {
            // QUAN TRỌNG: Bắt các lỗi nghiệp vụ từ Service
            // Ví dụ:
            // - 409 CONFLICT: "Phòng đã kín lịch..."
            // - 400 BAD_REQUEST: "Ngày check-out không hợp lệ..."
            // - 404 NOT_FOUND: "Khách hàng không tồn tại..."

            ApiResponse<Void> errorResponse = new ApiResponse<>(
                    false,
                    e.getStatus().value(),
                    e.getReason(), // Lấy message lỗi chi tiết từ Service
                    null,
                    "api/booking/create"
            );
            return ResponseEntity.status(e.getStatus()).body(errorResponse);

        } catch (Exception e) {
            // Bắt các lỗi hệ thống không mong muốn (500 Internal Server Error)
            e.printStackTrace();
            ApiResponse<Void> errorResponse = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Lỗi hệ thống: " + e.getMessage(),
                    null,
                    "api/booking/create"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllBookings(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "limit", required = false) Integer limit
    ) {
        List<BookingResponseDTO> result = bookingRoomService.getAllBookings(page, limit);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Integer id) {
        BookingResponseDTO result = bookingRoomService.getBookingById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Integer id) {
        bookingRoomService.cancelBooking(id);
        return ResponseEntity.ok("Hủy đặt phòng thành công!");
    }
}