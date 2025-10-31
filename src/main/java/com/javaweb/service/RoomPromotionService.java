package com.javaweb.service;

import java.util.Map;

import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionCreateDTO;
import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionResponseDTO;

public interface RoomPromotionService {
	RoomPromotionResponseDTO create(RoomPromotionCreateDTO dto);

	RoomPromotionResponseDTO update(Integer id, RoomPromotionCreateDTO dto);

	void delete(Integer id);

	RoomPromotionResponseDTO getById(Integer id);

	Map<String, Object> getAll(Integer page, Integer pageSize);

	Map<String, Object> search(Integer promotionId, Integer roomTypeId, Integer page, Integer pageSize);

}
