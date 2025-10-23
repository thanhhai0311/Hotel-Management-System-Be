package com.javaweb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
		if (token == null || token.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token không được để trống!");
		}

		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}

		if (!jwtUtil.validateToken(token)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token không hợp lệ hoặc đã hết hạn!");
		}

		String email = jwtUtil.extractEmail(token);

		System.out.println(email);

		AccountEntity account = accountRepository.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Không tìm thấy tài khoản tương ứng với email: " + email));

		if (account.getUser() == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tài khoản này chưa được gắn với người dùng nào!");
		}

		UserEntity user = userRepository.findById(account.getUser().getId()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin người dùng!"));

		return user;
	}

	@Override
	public UserEntity getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Người dùng chưa đăng nhập!");
		}

		String email = authentication.getName(); // username trong token

		return userRepository.findByAccount_Email(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Không tìm thấy người dùng có email: " + email));
	}

}
