package com.javaweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.RoomImageService;

@RestController
@RequestMapping("/api/room-images")
public class RoomImageController {

    @Autowired
    private RoomImageService roomImageService;

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> deleteRoomImageBySrc(@RequestParam String src) {
        roomImageService.deleteRoomImageBySrc(src);
        ApiResponse<Void> res = new ApiResponse<>(
                true, 200, "Xóa ảnh thành công", null, "api/room-images?src=" + src);
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoomImageById(@PathVariable Integer id) {
        roomImageService.deleteRoomImageById(id);
        ApiResponse<Void> res = new ApiResponse<>(
                true, 200, "Xóa ảnh thành công", null, "api/room-images/" + id);
        return ResponseEntity.ok(res);
    }
}
