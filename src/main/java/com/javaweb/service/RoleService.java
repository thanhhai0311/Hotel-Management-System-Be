package com.javaweb.service;

import java.util.List;

import com.javaweb.model.dto.RoleDTO.RoleDTO;
import com.javaweb.model.dto.RoleDTO.RoleResponseDTO;

public interface RoleService {

	RoleResponseDTO createRole(RoleDTO dto);

	List<RoleResponseDTO> getAllRoles();

	RoleResponseDTO getRoleById(Integer id);

	RoleResponseDTO updateRole(Integer id, RoleDTO dto);

	void deleteRole(Integer id);
}