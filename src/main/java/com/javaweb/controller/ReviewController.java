package com.javaweb.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
			ApiResponse<ReviewResponseDTO> apiRes = new ApiResponse<>(true, HttpStatus.CREATED.value(),
					"Tạo đánh giá thành công", response, "api/reviews/create");
			return ResponseEntity.status(HttpStatus.CREATED).body(apiRes);
		} catch (MultipartException e) {
			return ResponseEntity.badRequest()
					.body(new ApiResponse<>(false, 400, "Lỗi upload file", null, "api/reviews/create"));
		}
	}

	@PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
	public ResponseEntity<?> updateReview(@PathVariable Integer id, @ModelAttribute ReviewUpdateDTO dto)
			throws IOException {
//        try {
		ReviewResponseDTO updated = reviewService.updateReview(id, dto);
		ApiResponse<ReviewResponseDTO> response = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Cập nhật đánh giá thành công", updated, "api/reviews/update/" + id);
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

	@GetMapping("/getAll")
	public ResponseEntity<?> getAllReviews(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		try {
			Map<String, Object> result = reviewService.getAllReviews(page, size);

			ApiResponse<Map<String, Object>> response = new ApiResponse<>(true, HttpStatus.OK.value(),
					"Lấy danh sách đánh giá thành công", result, "api/reviews/getAll");

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> error = new HashMap<>();
			error.put("success", false);
			error.put("message", e.getMessage());
			return ResponseEntity.status(500).body(error);
		}
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<?> deleteReview(@PathVariable Integer id) {

		reviewService.deleteReview(id);
		ApiResponse<Void> response = new ApiResponse<>(true, HttpStatus.OK.value(), "Xóa đánh giá thành công", null,
				"api/reviews/" + id);

		return ResponseEntity.ok(response);

	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getReviewById(@PathVariable Integer id) {
		try {
			ReviewResponseDTO review = reviewService.getReviewById(id);

			ApiResponse<ReviewResponseDTO> response = new ApiResponse<>(true, HttpStatus.OK.value(),
					"Lấy thông tin đánh giá thành công", review, "api/reviews/" + id);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Lỗi khi lấy thông tin đánh giá: " + e.getMessage(), null, "api/reviews/" + id));
		}
	}
	
	@GetMapping("/search")
    public ResponseEntity<?> searchReviews(
            @RequestParam(required = false) String details,
            @RequestParam(required = false) Integer star,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer customerId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date day,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<ReviewResponseDTO> result = reviewService.searchReviews(details, star, type, customerId, day, page, size);

            ApiResponse<Page<ReviewResponseDTO>> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Tìm kiếm đánh giá thành công",
                    result,
                    "api/reviews/search"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

}
