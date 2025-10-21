package com.javaweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaweb.model.entity.AccountEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.repository.AccountRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.security.JwtUtil;

@Service
public class UserService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    public UserEntity getUserFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Token không hợp lệ!");
        }

        String email = jwtUtil.extractEmail(token);

        AccountEntity account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));

        UserEntity user = userRepository.findById(account.getUser().getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        return user;
    }
}