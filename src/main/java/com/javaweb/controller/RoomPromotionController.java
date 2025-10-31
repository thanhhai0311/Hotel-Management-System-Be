package com.javaweb.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionCreateDTO;
import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionResponseDTO;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.RoomPromotionService;

@RestController
@RequestMapping("/api/room-promotions")
public class RoomPromotionController {

	@Autowired
	private RoomPromotionService service;

	@GetMapping("/getAll")
	public ResponseEntity<ApiResponse<Map<String, Object>>> getAll(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize) {
		Map<String, Object> result = service.getAll(page, pageSize);
		return ResponseEntity
				.ok(new ApiResponse<>(true, 200, "Lấy danh sách thành công", result, "/api/room-promotions/getAll"));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<RoomPromotionResponseDTO>> getById(@PathVariable Integer id) {
		return ResponseEntity.ok(new ApiResponse<>(true, 200, "Lấy chi tiết thành công", service.getById(id),
				"/api/room-promotions/" + id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<ApiResponse<RoomPromotionResponseDTO>> create(@RequestBody RoomPromotionCreateDTO dto) {
		return ResponseEntity
				.ok(new ApiResponse<>(true, 201, "Tạo thành công", service.create(dto), "/api/room-promotions/create"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<ApiResponse<RoomPromotionResponseDTO>> update(@PathVariable Integer id,
			@RequestBody RoomPromotionCreateDTO dto) {
		return ResponseEntity.ok(new ApiResponse<>(true, 200, "Cập nhật thành công", service.update(id, dto),
				"/api/room-promotions/update/" + id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity
				.ok(new ApiResponse<>(true, 200, "Xóa thành công", null, "/api/room-promotions/delete/" + id));
	}

	@GetMapping("/search")
	public ResponseEntity<ApiResponse<Map<String, Object>>> search(@RequestParam(required = false) Integer promotionId,
			@RequestParam(required = false) Integer roomTypeId, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize) {
		Map<String, Object> result = service.search(promotionId, roomTypeId, page, pageSize);
		return ResponseEntity
				.ok(new ApiResponse<>(true, 200, "Tìm kiếm thành công", result, "/api/room-promotions/search"));
	}
}
