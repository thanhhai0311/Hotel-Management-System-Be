package com.javaweb.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.model.dto.AccountDTO;
import com.javaweb.model.dto.CreateAccountDTO;
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

}
