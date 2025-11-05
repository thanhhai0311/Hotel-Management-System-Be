package com.javaweb.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;

import com.javaweb.model.dto.ReviewDTO.ReviewCreateDTO;
import com.javaweb.model.dto.ReviewDTO.ReviewResponseDTO;
import com.javaweb.model.dto.ReviewDTO.ReviewUpdateDTO;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.ReviewService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public ResponseEntity<?> createReview(@ModelAttribute ReviewCreateDTO dto) throws IOException {
        try {
            ReviewResponseDTO response = reviewService.createReview(dto);
            ApiResponse<ReviewResponseDTO> apiRes = new ApiResponse<>(
                    true,
                    HttpStatus.CREATED.value(),
                    "Tạo đánh giá thành công",
                    response,
                    "api/reviews/create"
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(apiRes);
        } catch (MultipartException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, 400, "Lỗi upload file", null, "api/reviews/create"));
        }
    }
    
    @PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateReview(@PathVariable Integer id, @ModelAttribute ReviewUpdateDTO dto) throws IOException {
//        try {
            ReviewResponseDTO updated = reviewService.updateReview(id, dto);
            ApiResponse<ReviewResponseDTO> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Cập nhật đánh giá thành công",
                    updated,
                    "api/reviews/update/" + id
            );
            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            ApiResponse<Void> error = new ApiResponse<>(
//                    false,
//                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                    "Lỗi khi cập nhật đánh giá: " + e.getMessage(),
//                    null,
//                    "api/reviews/update/" + id
//            );
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//        }
    }
}
