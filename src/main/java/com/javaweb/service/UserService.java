package com.javaweb.service;

import com.javaweb.model.dto.UserDTO.UserProfileUpdateDTO;
import com.javaweb.model.dto.UserDTO.UserResponseDTO;
import com.javaweb.model.dto.UserDTO.UserUpdateDTO;
import com.javaweb.model.entity.UserEntity;

public interface UserService {
	UserEntity getUserFromToken(String token);
	UserEntity getCurrentUser();
	
	UserResponseDTO updateUser(Integer id, UserUpdateDTO dto);
	
	UserResponseDTO updateProfile(UserProfileUpdateDTO dto);
}
