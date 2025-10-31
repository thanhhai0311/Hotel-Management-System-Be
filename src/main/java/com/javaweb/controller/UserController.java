package com.javaweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.javaweb.mapper.UserMapper;
import com.javaweb.model.dto.UserDTO.UserProfileUpdateDTO;
import com.javaweb.model.dto.UserDTO.UserResponse;
import com.javaweb.model.dto.UserDTO.UserResponseDTO;
import com.javaweb.model.dto.UserDTO.UserUpdateDTO;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.UserService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * Lấy thông tin user từ token (dành cho frontend gọi sau khi login)
	 */
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserResponse>> getUserInfo(@RequestHeader("Authorization") String token) {

		UserEntity user = userService.getUserFromToken(token);
		UserResponse userResponse = UserMapper.toUserResponse(user);

		ApiResponse<UserResponse> response = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Lấy thông tin người dùng thành công", userResponse, "/api/user/me");

		return ResponseEntity.ok(response);
	}

	/**
	 * Lấy user hiện tại từ SecurityContext (token đã xác thực sẵn)
	 */
	@GetMapping
	public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
		UserEntity currentUser = userService.getCurrentUser();
		UserResponse userResponse = UserMapper.toUserResponse(currentUser);

		ApiResponse<UserResponse> response = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Lấy thông tin người dùng hiện tại thành công", userResponse, "/api/user");

		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}/update")
	public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UserUpdateDTO dto) {

		try {
			UserResponseDTO result = userService.updateUser(id, dto);
			ApiResponse<UserResponseDTO> response = new ApiResponse<>(true, HttpStatus.OK.value(),
					"Cập nhật thông tin cá nhân thành công", result, "api/users/" + id + "/update");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			ApiResponse<Void> error = new ApiResponse<>(false, 500, "Đã xảy ra lỗi: " + e.getMessage(), null,
					"api/users/" + id + "/update");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}
	
	@PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'STAFF')")
	@PutMapping("/profile")
	public ResponseEntity<?> updateProfile(@RequestBody UserProfileUpdateDTO dto) {
	    UserResponseDTO updated = userService.updateProfile(dto);
	    ApiResponse<UserResponseDTO> response = new ApiResponse<>(
	            true,
	            HttpStatus.OK.value(),
	            "Cập nhật thông tin cá nhân thành công",
	            updated,
	            "/api/users/profile"
	    );
	    return ResponseEntity.ok(response);
	}


}
