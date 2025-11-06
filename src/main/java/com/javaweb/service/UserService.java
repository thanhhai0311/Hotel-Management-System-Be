package com.javaweb.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.javaweb.model.dto.UserDTO.UserProfileUpdateDTO;
import com.javaweb.model.dto.UserDTO.UserResponseDTO;
import com.javaweb.model.dto.UserDTO.UserUpdateDTO;
import com.javaweb.model.dto.UserDTO.UserWithAccountResponseDTO;
import com.javaweb.model.entity.UserEntity;

public interface UserService {
	UserEntity getUserFromToken(String token);
	UserEntity getCurrentUser();
	
	UserResponseDTO updateUser(Integer id, UserUpdateDTO dto);
	
	UserResponseDTO updateProfile(UserProfileUpdateDTO dto);
	
	List<UserWithAccountResponseDTO> getAllUsers();

    Page<UserWithAccountResponseDTO> getAllUsers(int page, int size);

    List<UserWithAccountResponseDTO> getUsersByRole(Integer idRole);

    Page<UserWithAccountResponseDTO> getUsersByRole(Integer idRole, int page, int size);
    
    UserWithAccountResponseDTO getUserById(Integer id);
}
