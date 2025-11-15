package com.javaweb.controller;

import java.io.IOException;
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
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.model.dto.ServiceDTO.ServiceCreateDTO;
import com.javaweb.model.dto.ServiceDTO.ServiceResponseDTO;
import com.javaweb.model.dto.ServiceDTO.ServiceUpdateDTO;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.ServiceService;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

	@Autowired
	private ServiceService serviceService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/create", consumes = "multipart/form-data")
	public ResponseEntity<?> createService(@ModelAttribute ServiceCreateDTO dto) {
		try {
			ServiceResponseDTO created = serviceService.createService(dto);
			ApiResponse<ServiceResponseDTO> res = new ApiResponse<>(true, HttpStatus.CREATED.value(),
					"Tạo dịch vụ thành công", created, "api/services/create");
			return ResponseEntity.status(HttpStatus.CREATED).body(res);

		} catch (ResponseStatusException ex) {
			// bắt lỗi có chủ đích từ ServiceImpl
			ApiResponse<String> errorResponse = new ApiResponse<>(false, ex.getStatus().value(), ex.getReason(), null,
					"api/services/create");
			return ResponseEntity.status(ex.getStatus().value()).body(errorResponse);

		} catch (IOException e) {
			e.printStackTrace();
			ApiResponse<String> errorResponse = new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Lỗi xử lý file: " + e.getMessage(), null, "api/services/create");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

		} catch (Exception e) {
			e.printStackTrace();
			ApiResponse<String> errorResponse = new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Đã xảy ra lỗi không xác định: " + e.getMessage(), null, "api/services/create");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> getAllServices(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		try {
			Map<String, Object> data = serviceService.getAllServices(page, size);
			ApiResponse<Map<String, Object>> response = new ApiResponse<>(true, HttpStatus.OK.value(),
					"Lấy danh sách dịch vụ thành công", data, "api/services/getAll");
			return ResponseEntity.ok(response);
		} catch (ResponseStatusException ex) {
			ApiResponse<String> errorResponse = new ApiResponse<>(false, ex.getStatus().value(), ex.getReason(), null,
					"api/services/getAll");
			return ResponseEntity.status(ex.getStatus()).body(errorResponse);
		} catch (Exception e) {
			e.printStackTrace();
			ApiResponse<String> errorResponse = new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Đã xảy ra lỗi khi lấy danh sách dịch vụ: " + e.getMessage(), null, "api/services/getAll");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getServiceById(@PathVariable Integer id) {
		try {
			ServiceResponseDTO service = serviceService.getServiceById(id);
			ApiResponse<ServiceResponseDTO> res = new ApiResponse<>(true, HttpStatus.OK.value(),
					"Lấy thông tin dịch vụ thành công", service, "api/services/" + id);
			return ResponseEntity.ok(res);

		} catch (ResponseStatusException ex) {
			ApiResponse<String> errorResponse = new ApiResponse<>(false, ex.getStatus().value(), ex.getReason(), null,
					"api/services/" + id);
			return ResponseEntity.status(ex.getStatus()).body(errorResponse);

		} catch (Exception e) {
			e.printStackTrace();
			ApiResponse<String> errorResponse = new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Đã xảy ra lỗi khi lấy thông tin dịch vụ: " + e.getMessage(), null, "api/services/" + id);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
	public ResponseEntity<?> updateService(@PathVariable Integer id, @ModelAttribute ServiceUpdateDTO dto) {
		try {
			ServiceResponseDTO updated = serviceService.updateService(id, dto);
			ApiResponse<ServiceResponseDTO> res = new ApiResponse<>(true, HttpStatus.OK.value(),
					"Cập nhật dịch vụ thành công", updated, "api/services/update/" + id);
			return ResponseEntity.ok(res);

		} catch (ResponseStatusException ex) {
			ApiResponse<String> errorResponse = new ApiResponse<>(false, ex.getStatus().value(), ex.getReason(), null,
					"api/services/update/" + id);
			return ResponseEntity.status(ex.getStatus()).body(errorResponse);

		} catch (Exception e) {
			e.printStackTrace();
			ApiResponse<String> errorResponse = new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Đã xảy ra lỗi khi cập nhật dịch vụ: " + e.getMessage(), null, "api/services/update/" + id);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteService(@PathVariable Integer id) {
		try {
			serviceService.deleteService(id);
			ApiResponse<Void> response = new ApiResponse<>(true, HttpStatus.OK.value(), "Xóa dịch vụ thành công", null,
					"api/services/" + id);
			return ResponseEntity.ok(response);

		} catch (ResponseStatusException ex) {
			ApiResponse<String> errorResponse = new ApiResponse<>(false, ex.getStatus().value(), ex.getReason(), null,
					"api/services/" + id);
			return ResponseEntity.status(ex.getStatus()).body(errorResponse);

		} catch (Exception e) {
			e.printStackTrace();
			ApiResponse<String> errorResponse = new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Đã xảy ra lỗi khi xóa dịch vụ: " + e.getMessage(), null, "api/services/" + id);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchServices(@RequestParam(required = false) String name,
			@RequestParam(required = false) String details, @RequestParam(required = false) Float minPrice,
			@RequestParam(required = false) Float maxPrice, @RequestParam(required = false) Integer isAvaiable,
			@RequestParam(required = false) String unit, @RequestParam(required = false) Integer idHotel,
			@RequestParam(required = false) Integer idCategory, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {

		try {
			Page<ServiceResponseDTO> result = serviceService.searchServices(name, details, minPrice, maxPrice,
					isAvaiable, unit, idHotel, idCategory, page, size);

			ApiResponse<Page<ServiceResponseDTO>> response = new ApiResponse<>(true, HttpStatus.OK.value(),
					"Tìm kiếm dịch vụ thành công", result, "api/services/search");

			return ResponseEntity.ok(response);

		} catch (ResponseStatusException ex) {
			ApiResponse<String> errorResponse = new ApiResponse<>(false, ex.getStatus().value(), ex.getReason(), null,
					"api/services/search");
			return ResponseEntity.status(ex.getStatus()).body(errorResponse);

		} catch (Exception e) {
			e.printStackTrace();
			ApiResponse<String> errorResponse = new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
					"Đã xảy ra lỗi khi tìm kiếm dịch vụ: " + e.getMessage(), null, "api/services/search");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}

}
