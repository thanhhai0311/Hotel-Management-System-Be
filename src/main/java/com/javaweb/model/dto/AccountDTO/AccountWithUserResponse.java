package com.javaweb.model.dto.AccountDTO;

import com.javaweb.model.dto.UserDTO.UserResponse;

public class AccountWithUserResponse {
    private AccountDTO account;
    private UserResponse user;

    public AccountWithUserResponse() {}

    public AccountWithUserResponse(AccountDTO account, UserResponse user) {
        this.account = account;
        this.user = user;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
