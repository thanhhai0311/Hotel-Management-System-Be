package com.javaweb.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.converter.AccountConverter;
import com.javaweb.converter.UserConverter;
import com.javaweb.model.dto.AccountDTO.AccountDTO;
import com.javaweb.model.dto.AccountDTO.AdminUpdateUserDTO;
import com.javaweb.model.dto.AccountDTO.CreateAccountDTO;
import com.javaweb.model.dto.UserDTO.UserResponseDTO;
import com.javaweb.model.entity.AccountEntity;
import com.javaweb.model.entity.RoleEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.repository.AccountRepository;
import com.javaweb.repository.RoleRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccountConverter accountConverter;
	
	@Autowired
	private UserConverter userConverter;

	@Override
	@Transactional
	public AccountEntity toggleAccountActive(Integer id) {
		AccountEntity account = accountRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản với ID: " + id));

		if (account.isActive())
			account.setActive(false);
		else
			account.setActive(true);

		return accountRepository.save(account);
	}

	@Override
	public List<AccountDTO> getAllAccounts() {
		return accountRepository
				.findAll().stream().map(account -> new AccountDTO(account.getId(), account.getEmail(),
						account.isActive(), account.getRole() != null ? account.getRole().getName() : null))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public AccountEntity createAccount(CreateAccountDTO dto) {
		// Kiểm tra trùng email
		if (accountRepository.existsByEmail(dto.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại!");
		}

		RoleEntity role = roleRepository.findById(dto.getIdRole())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Không tìm thấy role với ID: " + dto.getIdRole()));

		AccountEntity account = new AccountEntity();
		account.setEmail(dto.getEmail());
		account.setPassword(passwordEncoder.encode(dto.getPassword()));
		account.setActive(true);
		account.setRole(role);
		accountRepository.save(account);
		
		AccountDTO dtoRes = new AccountDTO(account.getId(), account.getEmail(), account.isActive(),
				account.getRole() != null ? account.getRole().getName() : null);
		
		UserEntity user = new UserEntity();
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setGender(dto.getGender());
        user.setAddress(dto.getAddress());
        user.setDob(dto.getDob());
        user.setAccount(account);
        user.setIdentification(dto.getIdentification());
        
        userRepository.save(user);
        
        account.setUser(user);
        
		return account;
	}

	@Override
	public AccountEntity updateAccount(Integer id, AccountEntity account) {
		AccountEntity accountEntity = accountRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản với ID: " + id));

		// Kiểm tra trùng email nếu thay đổi
		if (!accountEntity.getEmail().equals(account.getEmail())
				&& accountRepository.existsByEmail(account.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại!");
		}

		accountEntity.setEmail(account.getEmail());

		if (account.getPassword() != null && !account.getPassword().isEmpty()) {
			accountEntity.setPassword(passwordEncoder.encode(account.getPassword()));
		}

		accountEntity.setActive(account.isActive());
		accountEntity.setRole(account.getRole());

		return accountRepository.save(accountEntity);
	}

	@Override
	public void deleteAccount(Integer id) {
		if (!accountRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản với ID: " + id);
		}

		accountRepository.deleteById(id);
	}

	@Override
	public AccountEntity updateAccountRole(Integer accountId, Integer roleId) {
		AccountEntity account = accountRepository.findById(accountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						"Không tìm thấy tài khoản với ID: " + accountId));

		RoleEntity role = roleRepository.findById(roleId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy role với ID: " + roleId));
		account.setRole(role);
		return accountRepository.save(account);
	}

//	@Override
//	public Object getAllAccounts(int page, int size, String keyword) {
//	    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
//	    Page<AccountEntity> accountPage;
//
//	    if (keyword == null || keyword.trim().isEmpty()) {
//	        accountPage = accountRepository.findAll(pageable);
//	    } else {
//	        accountPage = accountRepository.findByEmailContainingIgnoreCase(keyword, pageable);
//	    }
//
//	    // Chuyển entity sang DTO
//	    Page<AccountDTO> dtoPage = accountPage.map(account ->
//	            new AccountDTO(
//	                    account.getId(),
//	                    account.getEmail(),
//	                    account.isActive(),
//	                    account.getRole() != null ? account.getRole().getName() : null
//	            )
//	    );
//
//	    return dtoPage;
//	}
	
	@Override
	public Page<AccountDTO> getAllAccounts(Integer page, Integer size) {
	    try {
	        // 🔹 Nếu không truyền page/size hoặc truyền giá trị không hợp lệ → lấy toàn bộ
	        if (page == null || size == null || page < 0 || size < 0) {
	            List<AccountEntity> list = accountRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

	            List<AccountDTO> dtos = list.stream()
	                    .map(account -> new AccountDTO(
	                            account.getId(),
	                            account.getEmail(),
	                            account.isActive(),
	                            account.getRole() != null ? account.getRole().getName() : null
	                    ))
	                    .collect(Collectors.toList());

	            // Trả về Page giả để thống nhất kiểu dữ liệu
	            return new PageImpl<>(dtos);
	        }

	        // 🔹 Nếu có page và size → phân trang
	        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
	        Page<AccountEntity> accountPage = accountRepository.findAll(pageable);

	        return accountPage.map(account ->
	                new AccountDTO(
	                        account.getId(),
	                        account.getEmail(),
	                        account.isActive(),
	                        account.getRole() != null ? account.getRole().getName() : null
	                )
	        );

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new ResponseStatusException(
	                HttpStatus.INTERNAL_SERVER_ERROR,
	                "Lỗi khi lấy danh sách tài khoản: " + e.getMessage()
	        );
	    }
	}

	@Override
	public AccountDTO findById(Integer id) {
	    Optional<AccountEntity> optionalAccount = accountRepository.findById(id);
	    AccountEntity account = optionalAccount.orElseThrow(() -> 
	        new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy tài khoản"));
	    return accountConverter.toAccountDTO(account);
	}
	
	@Override
	@Transactional
	public UserResponseDTO updateUserByAdmin(Integer id, AdminUpdateUserDTO dto) {
	    // 🔹 Tìm user
	    UserEntity user = userRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(
	                    HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với ID: " + id));

	    AccountEntity account = user.getAccount();
	    if (account == null) {
	        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người dùng chưa có tài khoản liên kết");
	    }

	    // Cập nhật thông tin cá nhân (UserEntity)
	    if (dto.getName() != null) user.setName(dto.getName());
	    if (dto.getPhone() != null) user.setPhone(dto.getPhone());
	    if (dto.getGender() != null) user.setGender(dto.getGender());
	    if (dto.getAddress() != null) user.setAddress(dto.getAddress());
	    if (dto.getIdentification() != null) user.setIdentification(dto.getIdentification());
	    if (dto.getDob() != null) user.setDob(dto.getDob());

	    // Cập nhật thông tin tài khoản (AccountEntity)
	    if (dto.getEmail() != null && !dto.getEmail().equals(account.getEmail())) {
	        if (accountRepository.existsByEmail(dto.getEmail())) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email đã tồn tại!");
	        }
	        account.setEmail(dto.getEmail());
	    }

	    if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
	        if (dto.getPassword().length() < 6) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Mật khẩu phải có ít nhất 6 ký tự");
	        }
	        account.setPassword(passwordEncoder.encode(dto.getPassword()));
	    }

	    if (dto.getIsActive() != null) account.setActive(dto.getIsActive());

	    if (dto.getRoleId() != null) {
	        RoleEntity role = roleRepository.findById(dto.getRoleId())
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                        "Không tìm thấy quyền với ID = " + dto.getRoleId()));
	        account.setRole(role);
	    }

	    // Lưu
	    accountRepository.save(account);
	    userRepository.save(user);

	    return userConverter.toResponseDTO(user);
	}



}
