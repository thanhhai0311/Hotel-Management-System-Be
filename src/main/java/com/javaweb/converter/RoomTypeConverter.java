package com.javaweb.converter;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.javaweb.model.dto.RoomTypeDTO.RoomTypeCreateDTO;
import com.javaweb.model.dto.RoomTypeDTO.RoomTypeResponseDTO;
import com.javaweb.model.dto.RoomTypeDTO.RoomTypeUpdateDTO;
import com.javaweb.model.entity.RoomTypeEntity;
import com.javaweb.model.entity.TypeImageEntity;

@Component
public class RoomTypeConverter {

    // Convert CreateDTO -> Entity
    public RoomTypeEntity toEntity(RoomTypeCreateDTO dto) {
        RoomTypeEntity entity = new RoomTypeEntity();
        entity.setName(dto.getName());
        entity.setDetails(dto.getDetails());
        entity.setBedCount(dto.getBedCount());
        entity.setMaxOccupancy(dto.getMaxOccupancy());
        entity.setPrice(dto.getPrice());
        entity.setArea(dto.getArea());

        entity.setIsPrivateBathroom(dto.getIsPrivateBathroom());
        entity.setIsFreeToiletries(dto.getIsFreeToiletries());
        entity.setIsAirConditioning(dto.getIsAirConditioning());
        entity.setIsSoundproofing(dto.getIsSoundproofing());
        entity.setIsTV(dto.getIsTV());
        entity.setIsMiniBar(dto.getIsMiniBar());
        entity.setIsWorkDesk(dto.getIsWorkDesk());
        entity.setIsSeatingArea(dto.getIsSeatingArea());
        entity.setIsSafetyFeatures(dto.getIsSafetyFeatures());
        entity.setIsSmoking(dto.getIsSmoking());

        return entity;
    }

    // Convert Entity -> ResponseDTO
    public RoomTypeResponseDTO toResponseDTO(RoomTypeEntity entity) {
        RoomTypeResponseDTO dto = new RoomTypeResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDetails(entity.getDetails());
        dto.setBedCount(entity.getBedCount());
        dto.setMaxOccupancy(entity.getMaxOccupancy());
        dto.setPrice(entity.getPrice());
        dto.setArea(entity.getArea());

        dto.setIsPrivateBathroom(entity.getIsPrivateBathroom());
        dto.setIsFreeToiletries(entity.getIsFreeToiletries());
        dto.setIsAirConditioning(entity.getIsAirConditioning());
        dto.setIsSoundproofing(entity.getIsSoundproofing());
        dto.setIsTV(entity.getIsTV());
        dto.setIsMiniBar(entity.getIsMiniBar());
        dto.setIsWorkDesk(entity.getIsWorkDesk());
        dto.setIsSeatingArea(entity.getIsSeatingArea());
        dto.setIsSafetyFeatures(entity.getIsSafetyFeatures());
        dto.setIsSmoking(entity.getIsSmoking());
        dto.setIsDeleted(entity.getIsDeleted());
        
        if (entity.getTypeImages() != null) {
            dto.setImages(entity.getTypeImages().stream()
                    .map(TypeImageEntity::getSrc)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
    
    public void updateEntityFromDTO(RoomTypeEntity entity, RoomTypeUpdateDTO dto) {
        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            entity.setName(dto.getName().trim());
        }
        if (dto.getDetails() != null) entity.setDetails(dto.getDetails());
        if (dto.getBedCount() != null) entity.setBedCount(dto.getBedCount());
        if (dto.getMaxOccupancy() != null) entity.setMaxOccupancy(dto.getMaxOccupancy());
        if (dto.getPrice() != null) entity.setPrice(dto.getPrice());
        if (dto.getArea() != null) entity.setArea(dto.getArea());

        if (dto.getIsPrivateBathroom() != null) entity.setIsPrivateBathroom(dto.getIsPrivateBathroom());
        if (dto.getIsFreeToiletries() != null) entity.setIsFreeToiletries(dto.getIsFreeToiletries());
        if (dto.getIsAirConditioning() != null) entity.setIsAirConditioning(dto.getIsAirConditioning());
        if (dto.getIsSoundproofing() != null) entity.setIsSoundproofing(dto.getIsSoundproofing());
        if (dto.getIsTV() != null) entity.setIsTV(dto.getIsTV());
        if (dto.getIsMiniBar() != null) entity.setIsMiniBar(dto.getIsMiniBar());
        if (dto.getIsWorkDesk() != null) entity.setIsWorkDesk(dto.getIsWorkDesk());
        if (dto.getIsSeatingArea() != null) entity.setIsSeatingArea(dto.getIsSeatingArea());
        if (dto.getIsSafetyFeatures() != null) entity.setIsSafetyFeatures(dto.getIsSafetyFeatures());
        if (dto.getIsSmoking() != null) entity.setIsSmoking(dto.getIsSmoking());
        if(dto.getIsDeleted() != null) entity.setIsDeleted(dto.getIsDeleted());
    }
}
