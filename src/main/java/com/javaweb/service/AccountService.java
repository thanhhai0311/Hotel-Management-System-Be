package com.javaweb.service;

import com.javaweb.model.entity.AccountEntity;

public interface AccountService {
	AccountEntity toggleAccountActive(Long id);
}
