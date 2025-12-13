package com.javaweb.controller;

import com.javaweb.mapper.UserMapper;
import com.javaweb.model.dto.UserDTO.*;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
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
                    "Cập nhật thông tin cá nhân thành công", result, "api/user/" + id + "/update");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Void> error = new ApiResponse<>(false, 500, "Đã xảy ra lỗi: " + e.getMessage(), null,
                    "api/user/" + id + "/update");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN', 'STAFF')")
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody UserProfileUpdateDTO dto) {
        UserResponseDTO updated = userService.updateProfile(dto);
        ApiResponse<UserResponseDTO> response = new ApiResponse<>(true, HttpStatus.OK.value(),
                "Cập nhật thông tin cá nhân thành công", updated, "/api/user/profile");
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) Integer idRole,
                                         @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {

        Object data;
        String message;

        if (idRole == null) {
            message = "Lấy danh sách tất cả người dùng thành công";
            if (page != null && size != null)
                data = userService.getAllUsers(page, size);
            else
                data = userService.getAllUsers();
        } else {
            message = "Lấy danh sách người dùng theo vai trò thành công (idRole = " + idRole + ")";
            if (page != null && size != null)
                data = userService.getUsersByRole(idRole, page, size);
            else
                data = userService.getUsersByRole(idRole);
        }

        ApiResponse<Object> res = new ApiResponse<>(true, HttpStatus.OK.value(), message, data, "api/user/getAll");
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        UserWithAccountResponseDTO user = userService.getUserById(id);

        ApiResponse<UserWithAccountResponseDTO> res = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Lấy thông tin người dùng thành công",
                user,
                "api/users/" + id
        );

        return ResponseEntity.ok(res);
    }

}
