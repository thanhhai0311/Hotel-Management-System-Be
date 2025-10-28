package com.javaweb.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.javaweb.model.entity.AccountEntity;

public class CustomUserDetails implements UserDetails {

    private final AccountEntity accountEntity;

    public CustomUserDetails(AccountEntity accountEntity) {
        this.accountEntity = accountEntity;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + accountEntity.getRole().getName().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return accountEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return accountEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountEntity.isActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return accountEntity.isActive();
    }
}