package com.javaweb.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.model.dto.RoleDTO.RoleDTO;
import com.javaweb.model.dto.RoleDTO.RoleResponseDTO;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.RoleService;

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

	@Autowired
	private RoleService roleService;

	@PostMapping
	public ResponseEntity<ApiResponse<RoleResponseDTO>> createRole(@RequestBody RoleDTO dto) {
		try {
			RoleResponseDTO response = roleService.createRole(dto);
			ApiResponse<RoleResponseDTO> res = new ApiResponse<>(true, HttpStatus.CREATED.value(), "Tạo Role thành công",
					response, "/api/roles");
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			// Xử lý lỗi chung
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(),
							"Lỗi khi tạo Role: " + e.getMessage(), null, "/api/roles"));
		}
	}
	
	@GetMapping
	public ResponseEntity<ApiResponse<List<RoleResponseDTO>>> getAllRoles() {
		List<RoleResponseDTO> roles = roleService.getAllRoles();
		ApiResponse<List<RoleResponseDTO>> res = new ApiResponse<>(true, HttpStatus.OK.value(), "Lấy danh sách Role thành công",
				roles, "/api/roles");
		return ResponseEntity.ok(res);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<RoleResponseDTO>> getRoleById(@PathVariable Integer id) {
		// Dùng try-catch để bắt ResponseStatusException từ Service
		try {
			RoleResponseDTO role = roleService.getRoleById(id);
			ApiResponse<RoleResponseDTO> res = new ApiResponse<>(true, HttpStatus.OK.value(), "Lấy thông tin Role thành công",
					role, "/api/roles/" + id);
			return ResponseEntity.ok(res);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatus())
					.body(new ApiResponse<>(false, e.getStatus().value(), e.getReason(), null, "/api/roles/" + id));
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<RoleResponseDTO>> updateRole(@PathVariable Integer id, @RequestBody RoleDTO dto) {
		try {
			RoleResponseDTO updatedRole = roleService.updateRole(id, dto);
			ApiResponse<RoleResponseDTO> res = new ApiResponse<>(true, HttpStatus.OK.value(), "Cập nhật Role thành công",
					updatedRole, "/api/roles/" + id);
			return ResponseEntity.ok(res);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatus())
					.body(new ApiResponse<>(false, e.getStatus().value(), e.getReason(), null, "/api/roles/" + id));
		}
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable Integer id) {
		try {
			roleService.deleteRole(id);
			ApiResponse<Void> res = new ApiResponse<>(true, HttpStatus.OK.value(), "Xóa Role thành công", null,
					"/api/roles/" + id);
			return ResponseEntity.ok(res);
		} catch (ResponseStatusException e) {
			return ResponseEntity.status(e.getStatus())
					.body(new ApiResponse<>(false, e.getStatus().value(), e.getReason(), null, "/api/roles/" + id));
		}
	}
}