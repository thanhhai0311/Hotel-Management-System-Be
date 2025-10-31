package com.javaweb.converter;

import org.springframework.stereotype.Component;
import com.javaweb.model.dto.UserDTO.UserResponseDTO;
import com.javaweb.model.entity.UserEntity;

@Component
public class UserConverter {

    public UserResponseDTO toResponseDTO(UserEntity entity) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPhone(entity.getPhone());
        dto.setGender(entity.getGender());
        dto.setAddress(entity.getAddress());
        dto.setIdentification(entity.getIdentification());
        dto.setDob(entity.getDob());

        if (entity.getAccount() != null) {
            dto.setEmail(entity.getAccount().getEmail());
            dto.setActive(entity.getAccount().isActive());
            if (entity.getAccount().getRole() != null) {
                dto.setRoleName(entity.getAccount().getRole().getName());
            }
        }

        return dto;
    }
}
