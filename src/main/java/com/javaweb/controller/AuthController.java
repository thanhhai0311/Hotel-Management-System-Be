package com.javaweb.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.javaweb.model.dto.LoginRequest;
import com.javaweb.model.dto.LoginResponse;
import com.javaweb.model.dto.RegisterRequest;
import com.javaweb.model.entity.AccountEntity;
import com.javaweb.model.entity.RoleEntity;
import com.javaweb.repository.AccountRepository;
import com.javaweb.repository.RoleRepository;
import com.javaweb.security.JwtUtil;
import com.javaweb.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private AccountRepository accountRepository;
	
	@Autowired
    private JwtUtil jwtUtil;
	
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            AccountEntity account = accountRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));

            if (!account.isActive()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Tài khoản chưa được kích hoạt hoặc đã bị khóa");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            String token = jwtUtil.generateToken(account.getEmail(), role);
            
            String roleRes = "ROLE_" + account.getRole().getName().toUpperCase();

            return ResponseEntity.ok(new LoginResponse(token, roleRes));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai email hoặc mật khẩu");
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tài khoản đã bị khóa");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đăng nhập thất bại: " + e.getMessage());
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            String message = authService.register(request);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Đăng ký thất bại: " + e.getMessage());
        }
    }
}