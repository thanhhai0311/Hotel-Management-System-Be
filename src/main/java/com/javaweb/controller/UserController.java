package com.javaweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.javaweb.mapper.UserMapper;
import com.javaweb.model.dto.response.UserResponse;
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
    public ResponseEntity<ApiResponse<UserResponse>> getUserInfo(
            @RequestHeader("Authorization") String token) {

        UserEntity user = userService.getUserFromToken(token);
        UserResponse userResponse = UserMapper.toUserResponse(user);

        ApiResponse<UserResponse> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Lấy thông tin người dùng thành công",
                userResponse,
                "/api/user/me"
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Lấy user hiện tại từ SecurityContext (token đã xác thực sẵn)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        UserEntity currentUser = userService.getCurrentUser();
        UserResponse userResponse = UserMapper.toUserResponse(currentUser);

        ApiResponse<UserResponse> response = new ApiResponse<>(
                true,
                HttpStatus.OK.value(),
                "Lấy thông tin người dùng hiện tại thành công",
                userResponse,
                "/api/user"
        );

        return ResponseEntity.ok(response);
    }
}
