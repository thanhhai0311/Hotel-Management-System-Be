package com.javaweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<?> changeAccountStatus(@PathVariable Long id) {
		AccountEntity updated = accountService.toggleAccountActive(id);
		return ResponseEntity.ok().body("Account ID " + id + " is now " + (updated.isActive() ? "active" : "inactive"));
	}
}
