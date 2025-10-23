package com.javaweb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.javaweb.model.dto.AccountDTO;
import com.javaweb.model.dto.CreateAccountDTO;
import com.javaweb.model.entity.AccountEntity;
import com.javaweb.service.AccountService;

@RestController
@RequestMapping("/api/account")
public class AccountController {

	@Autowired
	private AccountService accountService;

	// Chỉ cho phép ADMIN gọi
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/active/{id}")
	public ResponseEntity<?> changeAccountStatus(@PathVariable Integer id) {
		AccountEntity updated = accountService.toggleAccountActive(id);
		return ResponseEntity.ok().body("Account ID " + id + " is now " + (updated.isActive() ? "active" : "inactive"));
	}
	
	@PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<?> createAccount(@RequestBody CreateAccountDTO dto) {
	    AccountEntity created = accountService.createAccount(dto);

	    // Trả DTO để tránh vòng lặp JSON
	    AccountDTO response = new AccountDTO(
	        created.getId(),
	        created.getEmail(),
	        created.isActive(),
	        created.getRole() != null ? created.getRole().getName() : null
	    );

	    return ResponseEntity.ok(response);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AccountEntity> updateAccount(@PathVariable Integer id, @RequestBody AccountEntity updatedAccount) {
        return ResponseEntity.ok(accountService.updateAccount(id, updatedAccount));
    }
	
	@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Integer id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok("Account with ID " + id + " deleted successfully");
    }
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}/updateRole")
	public ResponseEntity<?> updateAccountRole(@PathVariable Integer id, @RequestParam Integer roleId) {
	    AccountEntity updatedAccount = accountService.updateAccountRole(id, roleId);
	    return ResponseEntity.ok("Cập nhật role thành công cho tài khoản ID " + id +
	            " → Role mới: " + updatedAccount.getRole().getName());
	}
}
