package com.javaweb.converter;

import org.springframework.stereotype.Component;

import com.javaweb.model.dto.RoleDTO.RoleDTO;
import com.javaweb.model.entity.RoleEntity;

@Component
public class RoleConverter {

	public RoleDTO toDTO(RoleEntity entity) {
		RoleDTO dto = new RoleDTO();
		dto.setName(entity.getName());
		dto.setDetails(entity.getDetails());
		return dto;
	}

	public RoleEntity toEntity(RoleDTO dto) {
		RoleEntity entity = new RoleEntity();
		// Không set ID khi tạo mới
		entity.setName(dto.getName());
		entity.setDetails(dto.getDetails());
		return entity;
	}

	// Phương thức dùng cho việc update (cần entity cũ và dto mới)
	public RoleEntity toEntity(RoleEntity oldEntity, RoleDTO newDTO) {
		if (newDTO.getName() != null) {
			oldEntity.setName(newDTO.getName());
		}
		if (newDTO.getDetails() != null) {
			oldEntity.setDetails(newDTO.getDetails());
		}
		// Giữ nguyên ID, các trường khác nếu không được set sẽ giữ nguyên giá trị cũ
		return oldEntity;
	}
}