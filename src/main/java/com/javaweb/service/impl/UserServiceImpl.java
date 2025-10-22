package com.javaweb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.javaweb.model.entity.AccountEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.repository.AccountRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.security.JwtUtil;
import com.javaweb.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserEntity getUserFromToken(String token) {
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}

		if (!jwtUtil.validateToken(token)) {
			throw new RuntimeException("Token không hợp lệ!");
		}

		String email = jwtUtil.extractEmail(token);

		System.out.println(email);

		AccountEntity account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản!"));

		UserEntity user = userRepository.findById(account.getUser().getId())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

		return user;
	}

	@Override
	public UserEntity getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String email = authentication.getName(); // username trong token

		return userRepository.findByAccount_Email(email).orElseThrow(() -> new RuntimeException("User not found"));
	}

}
