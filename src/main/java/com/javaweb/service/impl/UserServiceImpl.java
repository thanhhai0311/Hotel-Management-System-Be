package com.javaweb.service.impl;

import com.javaweb.converter.UserConverter;
import com.javaweb.model.dto.UserDTO.UserProfileUpdateDTO;
import com.javaweb.model.dto.UserDTO.UserResponseDTO;
import com.javaweb.model.dto.UserDTO.UserUpdateDTO;
import com.javaweb.model.dto.UserDTO.UserWithAccountResponseDTO;
import com.javaweb.model.entity.AccountEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.repository.AccountRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.security.JwtUtil;
import com.javaweb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    @Override
    public UserResponseDTO updateUser(Integer id, UserUpdateDTO dto) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với ID: " + id));

        // Cập nhật các field user (nếu có)
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getGender() != null) user.setGender(dto.getGender());
        if (dto.getAddress() != null) user.setAddress(dto.getAddress());
        if (dto.getIdentification() != null) user.setIdentification(dto.getIdentification());
        if (dto.getDob() != null) user.setDob(dto.getDob());

        // Cập nhật account (email, password)
        AccountEntity account = user.getAccount();
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người dùng chưa có tài khoản liên kết");
        }

        if (dto.getEmail() != null) {
            // Kiểm tra email trùng lặp
            Optional<AccountEntity> existingOpt = accountRepository.findByEmail(dto.getEmail());
            if (existingOpt.isPresent()) {
                AccountEntity existing = existingOpt.get();
                if (!existing.getId().equals(account.getId())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Email đã tồn tại trong hệ thống");
                }
            }
            account.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null) {
            account.setPassword(dto.getPassword());
        }

        userRepository.save(user);
        return userConverter.toResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateProfile(UserProfileUpdateDTO dto) {
        // Lấy thông tin user đang đăng nhập
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Người dùng chưa đăng nhập");
        }

        String email = auth.getName();
        AccountEntity account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản"));

        UserEntity user = account.getUser();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy thông tin người dùng");
        }

        // Cập nhật thông tin cơ bản
        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getGender() != null) user.setGender(dto.getGender());
        if (dto.getAddress() != null) user.setAddress(dto.getAddress());
        if (dto.getIdentification() != null) user.setIdentification(dto.getIdentification());
        if (dto.getDob() != null) user.setDob(dto.getDob());

        // Đổi mật khẩu nếu có yêu cầu
        if (dto.getCurrentPassword() != null && dto.getNewPassword() != null) {
            if (!passwordEncoder.matches(dto.getCurrentPassword(), account.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu hiện tại không chính xác");
            }

            if (dto.getNewPassword().length() < 6) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu mới phải có ít nhất 6 ký tự");
            }

            account.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        }

        // Lưu lại thay đổi
        userRepository.save(user);
        accountRepository.save(account);

        return userConverter.toResponseDTO(user);
    }

    // ---------------- All users ----------------
    @Override
    public List<UserWithAccountResponseDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<UserWithAccountResponseDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return userRepository.findAll(pageable).map(this::toDTO);
    }

    // ---------------- By Role ----------------
    @Override
    public List<UserWithAccountResponseDTO> getUsersByRole(Integer idRole) {
        return userRepository.findByRoleId(idRole).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public Page<UserWithAccountResponseDTO> getUsersByRole(Integer idRole, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return userRepository.findByRoleId(idRole, pageable).map(this::toDTO);
    }

    private UserWithAccountResponseDTO toDTO(UserEntity user) {
        UserWithAccountResponseDTO dto = new UserWithAccountResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setGender(user.getGender());
        dto.setAddress(user.getAddress());
        dto.setIdentification(user.getIdentification());
        dto.setDob(user.getDob());

        if (user.getAccount() != null) {
            dto.setAccountId(user.getAccount().getId());
            dto.setEmail(user.getAccount().getEmail());
            dto.setActive(user.getAccount().isActive());
            if (user.getAccount().getRole() != null)
                dto.setRoleName(user.getAccount().getRole().getName());
        }

        if (user.getCustomerIdentification().getIdentificationImage() != null) {
            dto.setIdentificationImage(user.getCustomerIdentification().getIdentificationImage());
        }
        return dto;
    }

    @Override
    public UserWithAccountResponseDTO getUserById(Integer id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Không tìm thấy người dùng có ID = " + id));
        return toDTO(user);
    }


}
