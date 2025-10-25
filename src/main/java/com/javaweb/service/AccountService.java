package com.javaweb.service;

import java.util.List;

import com.javaweb.model.dto.AccountDTO;
import com.javaweb.model.dto.CreateAccountDTO;
import com.javaweb.model.entity.AccountEntity;
import com.javaweb.model.entity.RoleEntity;

public interface AccountService {
	AccountEntity toggleAccountActive(Integer id);
	List<AccountDTO> getAllAccounts();
    AccountEntity createAccount(CreateAccountDTO dto);
    AccountEntity updateAccount(Integer id, AccountEntity account);
    void deleteAccount(Integer id);
    AccountEntity updateAccountRole(Integer accountId, Integer roleId);
    Object getAllAccounts(int page, int size, String keyword);
	
}
