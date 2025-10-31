package com.javaweb.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.javaweb.model.dto.AuthDTO.LoginRequest;
import com.javaweb.model.dto.AuthDTO.LoginResponse;
import com.javaweb.model.dto.AuthDTO.RegisterRequest;
import com.javaweb.model.entity.AccountEntity;
import com.javaweb.repository.AccountRepository;
import com.javaweb.security.JwtUtil;
import com.javaweb.service.AuthService;
import com.javaweb.model.response.ApiResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    /**
     * ✅ API đăng nhập
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            AccountEntity account = accountRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản với email: " + request.getEmail()));

            if (!account.isActive()) {
                ApiResponse<LoginResponse> response = new ApiResponse<>(
                        false,
                        HttpStatus.FORBIDDEN.value(),
                        "Tài khoản chưa được kích hoạt hoặc đã bị khóa",
                        null,
                        httpRequest.getRequestURI()
                );
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            String token = jwtUtil.generateToken(account.getEmail(), role);

            String roleRes = "ROLE_" + account.getRole().getName().toUpperCase();

            LoginResponse data = new LoginResponse(token, roleRes);

            ApiResponse<LoginResponse> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    "Đăng nhập thành công!",
                    data,
                    httpRequest.getRequestURI()
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            ApiResponse<LoginResponse> response = new ApiResponse<>(
                    false,
                    HttpStatus.UNAUTHORIZED.value(),
                    "Sai email hoặc mật khẩu!",
                    null,
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (DisabledException e) {
            ApiResponse<LoginResponse> response = new ApiResponse<>(
                    false,
                    HttpStatus.FORBIDDEN.value(),
                    "Tài khoản đã bị vô hiệu hóa!",
                    null,
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        } catch (UsernameNotFoundException e) {
            ApiResponse<LoginResponse> response = new ApiResponse<>(
                    false,
                    HttpStatus.NOT_FOUND.value(),
                    e.getMessage(),
                    null,
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            ApiResponse<LoginResponse> response = new ApiResponse<>(
                    false,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Đăng nhập thất bại: " + e.getMessage(),
                    null,
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * ✅ API đăng ký người dùng mới
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
        try {
            String message = authService.register(request);

            ApiResponse<String> response = new ApiResponse<>(
                    true,
                    HttpStatus.OK.value(),
                    message,
                    null,
                    httpRequest.getRequestURI()
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(
                    false,
                    HttpStatus.BAD_REQUEST.value(),
                    "Đăng ký thất bại: " + e.getMessage(),
                    null,
                    httpRequest.getRequestURI()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
