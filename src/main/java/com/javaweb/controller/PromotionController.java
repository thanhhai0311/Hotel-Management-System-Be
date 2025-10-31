package com.javaweb.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.javaweb.model.dto.PromotionDTO.*;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.PromotionService;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

	@Autowired
	private PromotionService promotionService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/create", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<PromotionResponseDTO>> createPromotion(@ModelAttribute PromotionCreateDTO dto)
			throws IOException {
		PromotionResponseDTO result = promotionService.createPromotion(dto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, 201, "Tạo khuyến mãi thành công", result, "/api/promotions/create"));
	}

	@GetMapping("/getAll")
	public ResponseEntity<ApiResponse<Map<String, Object>>> getAllPromotions(
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
		Map<String, Object> result = promotionService.getAllPromotions(page, size);
		return ResponseEntity.ok(
				new ApiResponse<>(true, 200, "Lấy danh sách khuyến mãi thành công", result, "/api/promotions/getAll"));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<PromotionResponseDTO>> getPromotionById(@PathVariable Integer id) {
		PromotionResponseDTO result = promotionService.getPromotionById(id);
		return ResponseEntity.ok(
				new ApiResponse<>(true, 200, "Lấy thông tin khuyến mãi thành công", result, "/api/promotions/" + id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<PromotionResponseDTO>> updatePromotion(@PathVariable Integer id,
			@ModelAttribute PromotionUpdateDTO dto) throws IOException {
		PromotionResponseDTO result = promotionService.updatePromotion(id, dto);
		return ResponseEntity.ok(
				new ApiResponse<>(true, 200, "Cập nhật khuyến mãi thành công", result, "/api/promotions/update/" + id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deletePromotion(@PathVariable Integer id) {
		promotionService.deletePromotion(id);
		return ResponseEntity
				.ok(new ApiResponse<>(true, 200, "Xóa khuyến mãi thành công", null, "/api/promotions/" + id));
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<Page<PromotionResponseDTO>>> searchPromotions(
			@RequestParam(required = false) String keyword, @RequestParam(required = false) Boolean isActive,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {

		Page<PromotionResponseDTO> result = promotionService.searchPromotions(keyword, isActive, page, size);
		return ResponseEntity
				.ok(new ApiResponse<>(true, 200, "Tìm kiếm khuyến mãi thành công", result, "/api/promotions/search"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update-status/{id}")
	public ResponseEntity<ApiResponse<PromotionResponseDTO>> updateActiveStatus(@PathVariable Integer id,
			@RequestParam Boolean isActive) throws IOException {

		PromotionResponseDTO result = promotionService.updateActiveStatus(id, isActive);
		ApiResponse<PromotionResponseDTO> res = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Cập nhật trạng thái isActive thành công", result, "api/promotions/update-status/" + id);
		return ResponseEntity.ok(res);
	}

}
