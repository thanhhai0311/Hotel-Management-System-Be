package com.javaweb.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionCreateDTO;
import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionResponseDTO;
import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionUpdateDTO;

public interface RoomPromotionService {
	List<RoomPromotionResponseDTO> createRoomPromotion(RoomPromotionCreateDTO dto) throws IOException;

	RoomPromotionResponseDTO togglePromotionStatus(Integer id, boolean active);

	List<RoomPromotionResponseDTO> getActivePromotions(Integer page, Integer size);

	List<RoomPromotionResponseDTO> getUpcomingPromotions(Integer page, Integer size);

	List<RoomPromotionResponseDTO> getExpiredPromotions(Integer page, Integer size);

	Page<RoomPromotionResponseDTO> searchPromotions(Integer hotelId, String name, Boolean isActive, Boolean expired,
			int page, int size);
	
	RoomPromotionResponseDTO updateRoomPromotion(Integer id, RoomPromotionUpdateDTO dto) throws IOException;
	
	void deleteRoomPromotion(Integer id) throws IOException;

}
