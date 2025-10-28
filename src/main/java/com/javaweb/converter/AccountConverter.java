package com.javaweb.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.model.dto.AccountDTO;
import com.javaweb.model.entity.AccountEntity;
import com.javaweb.repository.AccountRepository;

@Component
public class AccountConverter {
	@Autowired
	private AccountRepository accountRepository;
	
	public AccountDTO toAccountDTO(AccountEntity accountEntity) {
		AccountDTO dto = new AccountDTO();
		dto.setId(accountEntity.getId());
		dto.setEmail(accountEntity.getEmail());
		dto.setActive(accountEntity.isActive());
		dto.setRoleName(accountEntity.getRole().getName());
		return dto;
	}
}
