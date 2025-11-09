package com.javaweb.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.javaweb.model.dto.ServiceCategoryDTO.ServiceCategoryRequest;
import com.javaweb.model.dto.ServiceCategoryDTO.ServiceCategoryResponse;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.ServiceCategoryService;

@RestController
@RequestMapping("/api/service-category")
public class ServiceCategoryController {

	@Autowired
	private ServiceCategoryService serviceCategoryService;

	@GetMapping
	public ResponseEntity<ApiResponse<Page<ServiceCategoryResponse>>> getAll(
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size,
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortDir) {

		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable = (page == null || size == null || page < 0 || size <= 0) ? Pageable.unpaged()
				: PageRequest.of(page, size, sort);
		Page<ServiceCategoryResponse> data = serviceCategoryService.getAll(pageable);
		ApiResponse<Page<ServiceCategoryResponse>> res = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Lấy danh sách thành công", data, "/api/service-category");
		return ResponseEntity.ok(res);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<ServiceCategoryResponse>> getById(@PathVariable Integer id) {
		ServiceCategoryResponse data = serviceCategoryService.getById(id);
		ApiResponse<ServiceCategoryResponse> res = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Lấy chi tiết thành công", data, "/api/service-category" + id);
		return ResponseEntity.ok(res);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ApiResponse<ServiceCategoryResponse>> create(@Valid @RequestBody ServiceCategoryRequest req) {
		ServiceCategoryResponse data = serviceCategoryService.create(req);
		ApiResponse<ServiceCategoryResponse> res = new ApiResponse<>(true, HttpStatus.CREATED.value(),
				"Tạo loại dịch vụ thành công", data, "/api/service-category");
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<ServiceCategoryResponse>> update(@PathVariable Integer id,
			@Valid @RequestBody ServiceCategoryRequest req) {
		ServiceCategoryResponse data = serviceCategoryService.update(id, req);
		ApiResponse<ServiceCategoryResponse> res = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Cập nhật loại dịch vụ thành công", data, "/api/service-category/" + id);
		return ResponseEntity.ok(res);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
		serviceCategoryService.delete(id);
		ApiResponse<Object> res = new ApiResponse<>(true, HttpStatus.OK.value(), "Xóa loại dịch vụ thành công", null,
				"/api/service-category/" + id);
		return ResponseEntity.ok(res);
	}

}
