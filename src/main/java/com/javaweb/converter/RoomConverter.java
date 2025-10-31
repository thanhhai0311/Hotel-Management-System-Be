package com.javaweb.converter;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.javaweb.model.dto.RoomDTO.RoomResponseDTO;
import com.javaweb.model.dto.RoomDTO.RoomResponseDTO.RoomTypeInfo;
import com.javaweb.model.entity.RoomEntity;
import com.javaweb.model.entity.RoomTypeEntity;

@Component
public class RoomConverter {

    public RoomResponseDTO toResponseDTO(RoomEntity entity) {
        RoomResponseDTO dto = new RoomResponseDTO();
        dto.setId(entity.getId());
        dto.setRoomNumber(entity.getRoomNumber());
        dto.setDetails(entity.getDetails());
        dto.setHotelName(entity.getHotel() != null ? entity.getHotel().getName() : null);
        dto.setRoomStatusName(entity.getRoomStatus() != null ? entity.getRoomStatus().getName() : null);

        // Map images
        if (entity.getRoomImage() != null && !entity.getRoomImage().isEmpty()) {
            dto.setImageUrls(
                entity.getRoomImage().stream()
                      .map(img -> img.getSrc())
                      .collect(Collectors.toList())
            );
        }

        // Map room type info (chi tiết)
        RoomTypeEntity type = entity.getRoomType();
        if (type != null) {
            RoomTypeInfo info = new RoomTypeInfo();
            info.setId(type.getId());
            info.setName(type.getName());
            info.setDetails(type.getDetails());
            info.setPrice(type.getPrice());
            info.setArea(type.getArea());
            info.setBedCount(type.getBedCount());
            info.setMaxOccupancy(type.getMaxOccupancy());

            info.setIsPrivateBathroom(type.getIsPrivateBathroom());
            info.setIsFreeToiletries(type.getIsFreeToiletries());
            info.setIsAirConditioning(type.getIsAirConditioning());
            info.setIsSoundproofing(type.getIsSoundproofing());
            info.setIsTV(type.getIsTV());
            info.setIsMiniBar(type.getIsMiniBar());
            info.setIsWorkDesk(type.getIsWorkDesk());
            info.setIsSeatingArea(type.getIsSeatingArea());
            info.setIsSafetyFeatures(type.getIsSafetyFeatures());
            info.setIsSmoking(type.getIsSmoking());

            dto.setRoomType(info);
        }

        return dto;
    }
}
