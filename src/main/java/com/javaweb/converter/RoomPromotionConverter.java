package com.javaweb.converter;

import org.springframework.stereotype.Component;
import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionResponseDTO;
import com.javaweb.model.entity.RoomEntity;
import com.javaweb.model.entity.RoomPromotionEntity;

@Component
public class RoomPromotionConverter {

    public RoomPromotionResponseDTO toResponseDTO(RoomPromotionEntity entity) {
        RoomPromotionResponseDTO dto = new RoomPromotionResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDetails(entity.getDetails());
        dto.setBanner(entity.getBanner());
        dto.setDiscount(entity.getDiscount());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setIsActive(entity.getIsActive());

        RoomEntity room = entity.getRoom();
        if (room != null) {
            dto.setRoomId(room.getId());
            dto.setRoomNumber(room.getRoomNumber());
            if (room.getHotel() != null) {
                dto.setHotelName(room.getHotel().getName());
            }
        }
        return dto;
    }
}
