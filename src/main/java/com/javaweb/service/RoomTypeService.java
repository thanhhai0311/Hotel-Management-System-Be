package com.javaweb.service;

import java.io.IOException;
import java.util.Map;

import com.javaweb.model.dto.RoomTypeDTO.RoomTypeCreateDTO;
import com.javaweb.model.dto.RoomTypeDTO.RoomTypeResponseDTO;
import com.javaweb.model.dto.RoomTypeDTO.RoomTypeUpdateDTO;

public interface RoomTypeService {
	RoomTypeResponseDTO createRoomType(RoomTypeCreateDTO dto) throws IOException;

	RoomTypeResponseDTO updateRoomType(Integer id, RoomTypeUpdateDTO dto) throws IOException;

	Map<String, Object> getAllRoomTypes(Integer page, Integer size);

	void deleteRoomType(Integer id);

	Map<String, Object> searchRoomTypes(String name, Float minPrice, Float maxPrice, Integer bedCount,
			Integer maxOccupancy, Float minArea, Float maxArea, Boolean isPrivateBathroom, Boolean isFreeToiletries,
			Boolean isMiniBar, Boolean isWorkDesk, Boolean isSeatingArea, Boolean isSafetyFeatures,
			Boolean isSoundproofing, Boolean isSmoking, Boolean isAirConditioning, Boolean isDeleted, Integer page,
			Integer size);

	RoomTypeResponseDTO getRoomTypeById(Integer id);

}
