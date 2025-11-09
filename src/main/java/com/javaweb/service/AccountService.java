package com.javaweb.service;

import java.util.List;

import com.javaweb.model.dto.AccountDTO.AccountDTO;
import com.javaweb.model.dto.AccountDTO.AdminUpdateUserDTO;
import com.javaweb.model.dto.AccountDTO.CreateAccountDTO;
import com.javaweb.model.dto.UserDTO.UserResponseDTO;
import com.javaweb.model.entity.AccountEntity;

public interface AccountService {
	AccountEntity toggleAccountActive(Integer id);
	List<AccountDTO> getAllAccounts();
    AccountEntity createAccount(CreateAccountDTO dto);
    AccountEntity updateAccount(Integer id, AccountEntity account);
    void deleteAccount(Integer id);
    AccountEntity updateAccountRole(Integer accountId, Integer roleId);
    Object getAllAccounts(Integer page, Integer size);
	AccountDTO findById(Integer id);
	UserResponseDTO updateUserByAdmin(Integer id, AdminUpdateUserDTO dto);

}
