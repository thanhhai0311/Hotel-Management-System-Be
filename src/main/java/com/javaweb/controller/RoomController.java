package com.javaweb.controller;

import com.javaweb.model.dto.RoomDTO.RoomCreateDTO;
import com.javaweb.model.dto.RoomDTO.RoomResponseDTO;
import com.javaweb.model.dto.RoomDTO.RoomUpdateDTO;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // CREATE ROOM
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<RoomResponseDTO>> createRoom(@ModelAttribute RoomCreateDTO dto)
            throws IOException {
        RoomResponseDTO result = roomService.createRoom(dto);
        ApiResponse<RoomResponseDTO> response = new ApiResponse<>(true, HttpStatus.CREATED.value(),
                "Tạo phòng thành công", result, "/api/rooms/create");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET ALL ROOMS
    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllRooms(@RequestParam(required = false) Integer page,
                                                                        @RequestParam(required = false) Integer size) {
        Map<String, Object> data = roomService.getAllRooms(page, size);
        ApiResponse<Map<String, Object>> response = new ApiResponse<>(true, 200, "Lấy danh sách phòng thành công", data,
                "/api/rooms/getAll");
        return ResponseEntity.ok(response);
    }

    // GET ROOM BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomResponseDTO>> getRoomById(@PathVariable Integer id) {
        RoomResponseDTO room = roomService.getRoomById(id);
        ApiResponse<RoomResponseDTO> response = new ApiResponse<>(true, HttpStatus.OK.value(),
                "Lấy thông tin phòng thành công", room, "/api/rooms/" + id);
        return ResponseEntity.ok(response);
    }

    // UPDATE ROOM
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<RoomResponseDTO>> updateRoom(@PathVariable Integer id,
                                                                   @ModelAttribute RoomUpdateDTO dto) throws IOException {
        RoomResponseDTO updated = roomService.updateRoom(id, dto);
        ApiResponse<RoomResponseDTO> response = new ApiResponse<>(true, HttpStatus.OK.value(),
                "Cập nhật phòng thành công", updated, "/api/rooms/update/" + id);
        return ResponseEntity.ok(response);
    }

    // DELETE ROOM
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoom(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        ApiResponse<Void> response = new ApiResponse<>(true, HttpStatus.OK.value(), "Xóa phòng thành công", null,
                "/api/rooms/" + id);
        return ResponseEntity.ok(response);
    }

    // SEARCH ROOMS
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<RoomResponseDTO>>> searchRooms(
            @RequestParam(required = false) Integer roomNumber, @RequestParam(required = false) Integer bedCount,
            @RequestParam(required = false) Integer maxOccupancy, @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice, @RequestParam(required = false) Integer hotelId,
            @RequestParam(required = false) Integer roomTypeId, @RequestParam(required = false) Integer roomStatusId,
            @RequestParam(required = false) String details, @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        Page<RoomResponseDTO> result = roomService.searchRooms(roomNumber, bedCount, maxOccupancy, minPrice, maxPrice,
                hotelId, roomTypeId, roomStatusId, details, page, size);

        ApiResponse<Page<RoomResponseDTO>> response = new ApiResponse<>(true, 200, "Tìm kiếm phòng thành công", result,
                "/api/rooms/search");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/room-available")
    public ResponseEntity<List<RoomResponseDTO>> searchRooms(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkIn,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOut
    ) {
        List<RoomResponseDTO> result = roomService.searchAvailableRooms(checkIn, checkOut);
        return ResponseEntity.ok(result);
    }
}
