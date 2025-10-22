package com.javaweb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaweb.model.entity.AccountEntity;
import com.javaweb.repository.AccountRepository;
import com.javaweb.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;

	@Override
	@Transactional
	public AccountEntity toggleAccountActive(Long id) {
		AccountEntity account = accountRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Account not found with ID: " + id));

		if(account.isActive()) account.setActive(false);
		else account.setActive(true);
		
		return accountRepository.save(account);
	}

}
