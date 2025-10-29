package com.javaweb.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionCreateDTO;
import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionResponseDTO;
import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionUpdateDTO;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.RoomPromotionService;

@RestController
@RequestMapping("/api/room-promotions")
public class RoomPromotionController {

	@Autowired
	private RoomPromotionService roomPromotionService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/create", consumes = "multipart/form-data")
	public ResponseEntity<?> createRoomPromotion(@ModelAttribute RoomPromotionCreateDTO dto) throws IOException {
//        try {
		List<RoomPromotionResponseDTO> createdPromotions = roomPromotionService.createRoomPromotion(dto);
		ApiResponse<List<RoomPromotionResponseDTO>> response = new ApiResponse<>(true, HttpStatus.CREATED.value(),
				"Tạo khuyến mãi phòng thành công cho " + createdPromotions.size() + " phòng", createdPromotions,
				"api/room-promotions/create");
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Map<String, Object> error = new HashMap<>();
//            error.put("success", false);
//            error.put("status", 500);
//            error.put("message", "Đã xảy ra lỗi: " + e.getMessage());
//            return ResponseEntity.status(500).body(error);
//        }
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}/toggle")
	public ResponseEntity<?> togglePromotion(@PathVariable Integer id, @RequestParam boolean active) {
//        try {
		RoomPromotionResponseDTO updated = roomPromotionService.togglePromotionStatus(id, active);

		ApiResponse<RoomPromotionResponseDTO> response = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Cập nhật trạng thái khuyến mãi thành công", updated, "api/room-promotions/" + id + "/toggle");

		return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Map<String, Object> error = new HashMap<>();
//            error.put("success", false);
//            error.put("status", 500);
//            error.put("message", "Đã xảy ra lỗi: " + e.getMessage());
//            return ResponseEntity.status(500).body(error);
//        }
	}

	@GetMapping("/active")
	public ResponseEntity<?> getActivePromotions(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {

		List<RoomPromotionResponseDTO> data = roomPromotionService.getActivePromotions(page, size);
		ApiResponse<List<RoomPromotionResponseDTO>> res = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Lấy danh sách khuyến mãi đang hoạt động thành công", data, "api/room-promotions/active");
		return ResponseEntity.ok(res);
	}

	@GetMapping("/upcoming")
	public ResponseEntity<?> getUpcomingPromotions(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {

		List<RoomPromotionResponseDTO> data = roomPromotionService.getUpcomingPromotions(page, size);
		ApiResponse<List<RoomPromotionResponseDTO>> res = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Lấy danh sách khuyến mãi sắp diễn ra thành công", data, "api/room-promotions/upcoming");
		return ResponseEntity.ok(res);
	}

	@GetMapping("/expired")
	public ResponseEntity<?> getExpiredPromotions(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {

		List<RoomPromotionResponseDTO> data = roomPromotionService.getExpiredPromotions(page, size);
		ApiResponse<List<RoomPromotionResponseDTO>> res = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Lấy danh sách khuyến mãi đã hết hạn thành công", data, "api/room-promotions/expired");
		return ResponseEntity.ok(res);
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchPromotions(@RequestParam(required = false) Integer hotelId,
			@RequestParam(required = false) String name, @RequestParam(required = false) Boolean isActive,
			@RequestParam(required = false) Boolean expired, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Page<RoomPromotionResponseDTO> result = roomPromotionService.searchPromotions(hotelId, name, isActive, expired,
				page, size);

		ApiResponse<Page<RoomPromotionResponseDTO>> res = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Tìm kiếm khuyến mãi thành công", result, "api/room-promotions/search");
		return ResponseEntity.ok(res);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
	public ResponseEntity<?> updateRoomPromotion(@PathVariable Integer id, @ModelAttribute RoomPromotionUpdateDTO dto)
			throws IOException {

		RoomPromotionResponseDTO updated = roomPromotionService.updateRoomPromotion(id, dto);
		ApiResponse<RoomPromotionResponseDTO> res = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Cập nhật khuyến mãi thành công", updated, "api/room-promotions/update/" + id);
		return ResponseEntity.ok(res);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteRoomPromotion(@PathVariable Integer id) throws IOException {
		roomPromotionService.deleteRoomPromotion(id);

		ApiResponse<String> res = new ApiResponse<>(true, HttpStatus.OK.value(), "Xóa khuyến mãi thành công",
				"Khuyến mãi ID " + id + " đã được xóa", "api/room-promotions/delete/" + id);
		return ResponseEntity.ok(res);
	}

}
