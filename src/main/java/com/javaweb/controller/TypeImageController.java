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
import com.javaweb.service.TypeImageService;

@RestController
@RequestMapping("/api/room-type-images")
public class TypeImageController {

    @Autowired
    private TypeImageService typeImageService;

    /**
     * Xóa ảnh theo src
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<?> deleteBySrc(@RequestParam String src) {
        typeImageService.deleteTypeImageBySrc(src);
        ApiResponse<Void> res = new ApiResponse<>(
                true, 200, "Xóa ảnh loại phòng thành công (Cloudinary + DB)", null,
                "api/room-type-images?src=" + src);
        return ResponseEntity.ok(res);
    }

    /**
     * Xóa ảnh theo id
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        typeImageService.deleteTypeImageById(id);
        ApiResponse<Void> res = new ApiResponse<>(
                true, 200, "Xóa ảnh loại phòng thành công (Cloudinary + DB)", null,
                "api/room-type-images/" + id);
        return ResponseEntity.ok(res);
    }
}
