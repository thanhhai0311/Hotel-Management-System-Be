package com.javaweb.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.mapper.UserMapper;
import com.javaweb.model.dto.AccountDTO;
import com.javaweb.model.dto.CreateAccountDTO;
import com.javaweb.model.dto.response.AccountWithUserResponse;
import com.javaweb.model.dto.response.UserResponse;
import com.javaweb.model.entity.AccountEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.AccountService;

@RestController
@RequestMapping("/api/account")
public class AccountController {

	@Autowired
	private AccountService accountService;

	/**
	 * Bật/tắt trạng thái tài khoản
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/active/{id}")
	public ResponseEntity<ApiResponse<AccountDTO>> changeAccountStatus(@PathVariable Integer id) {
		AccountEntity updated = accountService.toggleAccountActive(id);

		AccountDTO dto = new AccountDTO(updated.getId(), updated.getEmail(), updated.isActive(),
				updated.getRole() != null ? updated.getRole().getName() : null);

		ApiResponse<AccountDTO> response = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Thay đổi trạng thái tài khoản thành công", dto, "/api/account/active/" + id);

		return ResponseEntity.ok(response);
	}

	/**
	 * Lấy danh sách tất cả tài khoản
	 */
//	@PreAuthorize("hasRole('ADMIN')")
//	@GetMapping
//	public ResponseEntity<ApiResponse<List<AccountDTO>>> getAllAccounts() {
//		List<AccountDTO> accounts = accountService.getAllAccounts();
//
//		ApiResponse<List<AccountDTO>> response = new ApiResponse<>(true, HttpStatus.OK.value(),
//				"Lấy danh sách tài khoản thành công", accounts, "/api/account");
//
//		return ResponseEntity.ok(response);
//	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<ApiResponse<Object>> getAllAccounts(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(required = false) String keyword) {

	    // Gọi service xử lý
	    Object accountsPage = accountService.getAllAccounts(page, size, keyword);

	    ApiResponse<Object> response = new ApiResponse<>(
	            true,
	            HttpStatus.OK.value(),
	            "Lấy danh sách tài khoản thành công",
	            accountsPage,
	            "/api/account"
	    );

	    return ResponseEntity.ok(response);
	}

	/**
	 * Tạo tài khoản mới
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ApiResponse<AccountWithUserResponse>> createAccount(@Valid @RequestBody CreateAccountDTO dto) {
		AccountEntity created = accountService.createAccount(dto);

		// Lấy thông tin user từ Account
		UserEntity user = created.getUser();

		// Mapping sang DTO
		AccountDTO accountDTO = new AccountDTO(created.getId(), created.getEmail(), created.isActive(),
				created.getRole() != null ? created.getRole().getName() : null);

		UserResponse userResponse = UserMapper.toUserResponse(user);

		// Gộp lại
		AccountWithUserResponse combined = new AccountWithUserResponse(accountDTO, userResponse);

		ApiResponse<AccountWithUserResponse> response = new ApiResponse<>(true, HttpStatus.CREATED.value(),
				"Tạo tài khoản & người dùng thành công", combined, "/api/account");

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * Cập nhật thông tin tài khoản
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<AccountDTO>> updateAccount(@PathVariable Integer id,
			@RequestBody AccountEntity updatedAccount) {

		AccountEntity account = accountService.updateAccount(id, updatedAccount);

		AccountDTO dto = new AccountDTO(account.getId(), account.getEmail(), account.isActive(),
				account.getRole() != null ? account.getRole().getName() : null);

		ApiResponse<AccountDTO> response = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Cập nhật tài khoản thành công", dto, "/api/account/" + id);

		return ResponseEntity.ok(response);
	}

	/**
	 * Xóa tài khoản
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable Integer id) {
		accountService.deleteAccount(id);

		ApiResponse<Void> response = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Xóa tài khoản thành công (ID: " + id + ")", null, "/api/account/" + id);

		return ResponseEntity.ok(response);
	}

	/**
	 * Cập nhật role của tài khoản
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}/updateRole")
	public ResponseEntity<ApiResponse<AccountDTO>> updateAccountRole(@PathVariable Integer id,
			@RequestParam Integer roleId) {

		AccountEntity updatedAccount = accountService.updateAccountRole(id, roleId);

		AccountDTO dto = new AccountDTO(updatedAccount.getId(), updatedAccount.getEmail(), updatedAccount.isActive(),
				updatedAccount.getRole() != null ? updatedAccount.getRole().getName() : null);

		ApiResponse<AccountDTO> response = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Cập nhật quyền thành công cho tài khoản ID: " + id, dto, "/api/account/" + id + "/updateRole");

		return ResponseEntity.ok(response);
	}
}
