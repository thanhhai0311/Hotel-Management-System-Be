package com.javaweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.ReviewImageService;

@RestController
@RequestMapping("/api/review-images")
public class ReviewImageController {

    @Autowired
    private ReviewImageService reviewImageService;

    @DeleteMapping("")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<?> deleteImageBySrc(@RequestParam("src") String src) {
        try {
            reviewImageService.deleteImageBySrc(src);

            ApiResponse<Void> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Xóa ảnh review thành công",
                    null,
                    "api/review-images/deleteBySrc"
            );

            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(
                    new ApiResponse<>(false, e.getStatus().value(), e.getReason(), null,
                            "api/review-images/deleteBySrc")
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Lỗi khi xóa ảnh review: " + e.getMessage(),
                            null, "api/review-images/deleteBySrc")
            );
        }
    }
}
