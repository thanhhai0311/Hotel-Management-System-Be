package com.javaweb.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.javaweb.model.entity.RoomEntity;
import com.javaweb.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Thêm phòng mới + upload nhiều ảnh
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<RoomEntity> createRoom(
            @RequestPart("room") RoomEntity room,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {

        return ResponseEntity.ok(roomService.createRoom(room, images));
    }

    // Lấy danh sách phòng
    @GetMapping
    public ResponseEntity<List<RoomEntity>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    // Lấy chi tiết phòng
    @GetMapping("/{id}")
    public ResponseEntity<RoomEntity> getRoomById(@PathVariable Integer id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    // Sửa phòng
    @PutMapping("/{id}")
    public ResponseEntity<RoomEntity> updateRoom(@PathVariable Integer id, @RequestBody RoomEntity room) {
        return ResponseEntity.ok(roomService.updateRoom(id, room));
    }

    // Xóa phòng (xóa luôn ảnh)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }
}
