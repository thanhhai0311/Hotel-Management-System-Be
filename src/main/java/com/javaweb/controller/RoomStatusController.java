package com.javaweb.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.javaweb.model.dto.RoomStatusDTO.RoomStatusRequest;
import com.javaweb.model.dto.RoomStatusDTO.RoomStatusResponse;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.RoomStatusService;

@RestController
@RequestMapping("/api/room-statuses")
public class RoomStatusController {

	private final RoomStatusService roomStatusService;

	public RoomStatusController(RoomStatusService roomStatusService) {
		this.roomStatusService = roomStatusService;
	}

	@GetMapping
	public ResponseEntity<ApiResponse<Page<RoomStatusResponse>>> getAll(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "desc") String sortDir) {

		// 🔹 Xử lý sort direction
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

		// 🔹 Nếu không truyền page/size hoặc truyền giá trị không hợp lệ → lấy toàn bộ
		Pageable pageable = (page == null || size == null || page < 0 || size <= 0) ? Pageable.unpaged()
				: PageRequest.of(page, size, sort);

		// 🔹 Gọi service (không cần keyword)
		Page<RoomStatusResponse> data = roomStatusService.getAll(pageable);

		// 🔹 Gói kết quả vào ApiResponse
		ApiResponse<Page<RoomStatusResponse>> res = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Lấy danh sách thành công", data, "/api/room-statuses");

		return ResponseEntity.ok(res);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<RoomStatusResponse>> getById(@PathVariable Integer id) {
		RoomStatusResponse data = roomStatusService.getById(id);
		ApiResponse<RoomStatusResponse> res = new ApiResponse<>(true, HttpStatus.OK.value(), "Lấy chi tiết thành công",
				data, "/api/room-statuses/" + id);
		return ResponseEntity.ok(res);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ApiResponse<RoomStatusResponse>> create(@Valid @RequestBody RoomStatusRequest req) {
		RoomStatusResponse data = roomStatusService.create(req);
		ApiResponse<RoomStatusResponse> res = new ApiResponse<>(true, HttpStatus.CREATED.value(),
				"Tạo trạng thái thành công", data, "/api/room-statuses");
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<RoomStatusResponse>> update(@PathVariable Integer id,
			@Valid @RequestBody RoomStatusRequest req) {
		RoomStatusResponse data = roomStatusService.update(id, req);
		ApiResponse<RoomStatusResponse> res = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Cập nhật trạng thái thành công", data, "/api/room-statuses/" + id);
		return ResponseEntity.ok(res);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
		roomStatusService.delete(id);
		ApiResponse<Object> res = new ApiResponse<>(true, HttpStatus.OK.value(), "Xóa trạng thái thành công", null,
				"/api/room-statuses/" + id);
		return ResponseEntity.ok(res);
	}
}
