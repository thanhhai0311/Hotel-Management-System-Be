package com.javaweb.mapper;

import com.javaweb.model.dto.response.UserResponse;
import com.javaweb.model.entity.AccountEntity;
import com.javaweb.model.entity.RoleEntity;
import com.javaweb.model.entity.UserEntity;

public class UserMapper {

    public static UserResponse toUserResponse(UserEntity entity) {
        if (entity == null) return null;

        AccountEntity account = entity.getAccount();
        RoleEntity role = (account != null) ? account.getRole() : null;

        return new UserResponse(
                entity.getId(),
                entity.getName(),
                entity.getPhone(),
                entity.getGender(),
                entity.getAddress(),
                entity.getDob(),
                account != null ? account.getEmail() : null,
                role != null ? role.getName() : null
        );
    }
}