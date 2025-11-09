package com.javaweb.service;

import java.util.List;

import com.javaweb.model.dto.RoleDTO.RoleDTO;

public interface RoleService {

	RoleDTO createRole(RoleDTO dto);

	List<RoleDTO> getAllRoles();

	RoleDTO getRoleById(Integer id);

	RoleDTO updateRole(Integer id, RoleDTO dto);

	void deleteRole(Integer id);
}