package com.javaweb.controller;

import com.javaweb.model.dto.ReviewDTO.ReviewDTO;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

//    @PostMapping
//    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO dto) {
//        return ResponseEntity.ok(reviewService.createReview(dto));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Integer id, @RequestBody ReviewDTO dto) {
//        return ResponseEntity.ok(reviewService.updateReview(id, dto));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Integer id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    @GetMapping
    public ResponseEntity<?> getAllReviews(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize) {
        try {
            Map<String, Object> result = reviewService.getAllReviews(page, pageSize);

            ApiResponse<Map<String, Object>> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Lấy danh sách đánh giá thành công",
                    result,
                    "/api/reviews"
            );

            return ResponseEntity.ok(response);
        } catch (ResponseStatusException ex) {
            ApiResponse<Void> error = new ApiResponse<>(
                    false,
                    ex.getStatus().value(),
                    ex.getReason(),
                    null,
                    "/api/reviews"
            );
            return ResponseEntity.status(ex.getStatus()).body(error);
        } catch (Exception e) {
            ApiResponse<Void> error = new ApiResponse<>(
                    false,
                    500,
                    "Đã xảy ra lỗi: " + e.getMessage(),
                    null,
                    "/api/reviews"
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
