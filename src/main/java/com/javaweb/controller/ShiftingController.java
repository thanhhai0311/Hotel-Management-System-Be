package com.javaweb.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.javaweb.model.dto.ShiftingDTO.ShiftingCreateDTO;
import com.javaweb.model.dto.ShiftingDTO.ShiftingUpdateDTO;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.ShiftingService;

@RestController
@RequestMapping("/api/shiftings")
public class ShiftingController {

	@Autowired
	private ShiftingService shiftingService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public ResponseEntity<?> create(@RequestBody ShiftingCreateDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, 201, "Phân ca thành công",
				shiftingService.create(dto), "api/shiftings/create"));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/update/{id}")
	public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody ShiftingUpdateDTO dto) {
		return ResponseEntity.ok(new ApiResponse<>(true, 200, "Cập nhật phân ca thành công",
				shiftingService.update(id, dto), "api/shiftings/update/" + id));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		shiftingService.delete(id);
		return ResponseEntity.ok(new ApiResponse<>(true, 200, "Xóa phân ca thành công", null, "api/shiftings/" + id));
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Integer id) {
		return ResponseEntity.ok(new ApiResponse<>(true, 200, "Lấy phân ca thành công", shiftingService.getById(id),
				"api/shiftings/" + id));
	}

	@GetMapping("/search")
	public ResponseEntity<?> search(@RequestParam(required = false) Integer idEmployee,
			@RequestParam(required = false) Integer idShift,
			@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate day,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {

		Object result;
		if (page == null || pageSize == null) {
			result = shiftingService.searchAll(idEmployee, idShift, day);
		} else {
			result = shiftingService.search(idEmployee, idShift, day, page, pageSize);
		}

		return ResponseEntity
				.ok(new ApiResponse<>(true, 200, "Tìm kiếm phân ca thành công", result, "api/shiftings/search"));
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> getAll(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize) {

		Object result;
		if (page == null || pageSize == null) {
			result = shiftingService.getAll();
		} else {
			result = shiftingService.getAllPaged(page, pageSize);
		}

		return ResponseEntity
				.ok(new ApiResponse<>(true, 200, "Lấy danh sách phân ca thành công", result, "api/shiftings/getAll"));
	}

}
