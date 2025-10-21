package com.javaweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.javaweb.model.dto.RegisterRequest;
import com.javaweb.model.entity.AccountEntity;
import com.javaweb.model.entity.RoleEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.repository.AccountRepository;
import com.javaweb.repository.RoleRepository;
import com.javaweb.repository.UserRepository;

@Service
public class AuthService {
	@Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String register(RegisterRequest request) {
        // 1 Kiểm tra email đã tồn tại chưa
        if (accountRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        // 2️ Tìm role theo tên (CUSTOMER / STAFF / ADMIN)
        RoleEntity role = roleRepository.findByName(request.getRoleName().toUpperCase())
                .orElseThrow(() -> new RuntimeException("Role không tồn tại!"));

        // 3️ Tạo account
        AccountEntity account = new AccountEntity();
        account.setEmail(request.getEmail());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setActive(true);
        account.setRole(role);

        accountRepository.save(account);

        // 4️ Tạo user (thông tin cá nhân)
        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setGender(request.getGender());
        user.setAccount(account);
        user.setDob(request.getDob());

        userRepository.save(user);

        return "Đăng ký thành công!";
    }
}
