package com.javaweb.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.dto.ShiftDTO.ShiftCreateDTO;
import com.javaweb.model.dto.ShiftDTO.ShiftResponseDTO;
import com.javaweb.model.dto.ShiftDTO.ShiftUpdateDTO;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.ShiftService;

@RestController
@RequestMapping("/api/shifts")
public class ShiftController {

	@Autowired
	private ShiftService shiftService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<?> createShift(@RequestBody ShiftCreateDTO dto) {
		ShiftResponseDTO response = shiftService.createShift(dto);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new ApiResponse<>(true, 201, "Tạo ca làm thành công", response, "api/shifts/create"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/getAll")
	public ResponseEntity<?> getAll(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		Map<String, Object> data = shiftService.getAllShifts(page, size);
		return ResponseEntity
				.ok(new ApiResponse<>(true, 200, "Lấy danh sách ca làm thành công", data, "api/shifts/getAll"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		ShiftResponseDTO dto = shiftService.getShiftById(id);
		return ResponseEntity
				.ok(new ApiResponse<>(true, 200, "Lấy thông tin ca làm thành công", dto, "api/shifts/" + id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateShift(@PathVariable Integer id, @RequestBody ShiftUpdateDTO dto) {
		ShiftResponseDTO response = shiftService.updateShift(id, dto);
		return ResponseEntity
				.ok(new ApiResponse<>(true, 200, "Cập nhật ca làm thành công", response, "api/shifts/update/" + id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteShift(@PathVariable Integer id) {
		shiftService.deleteShift(id);
		return ResponseEntity.ok(new ApiResponse<>(true, 200, "Xóa ca làm thành công", null, "api/shifts/" + id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/search")
	public ResponseEntity<?> searchShifts(@RequestParam(required = false) String name,
			@RequestParam(required = false) String details, @RequestParam(required = false) Boolean isActive,
			@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "50") Integer size) {

		Object result;

		if (page == null || size == null) {
			// Nếu không truyền page và size -> lấy tất cả
			result = shiftService.searchAllShifts(name, details, isActive);
		} else {
			// Có truyền -> phân trang
			result = shiftService.searchShifts(name, details, isActive, page, size);
		}
		return ResponseEntity
				.ok(new ApiResponse<>(true, 200, "Tìm kiếm ca làm thành công", result, "api/shifts/search"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update-active/{id}")
	public ResponseEntity<?> updateShiftActive(@PathVariable Integer id, @RequestParam Boolean isActive) {
		ShiftResponseDTO res = shiftService.updateShiftActive(id, isActive);
		return ResponseEntity.ok(new ApiResponse<>(true, 200, "Cập nhật trạng thái ca làm thành công", res,
				"api/shifts/update-active/" + id));
	}

}
