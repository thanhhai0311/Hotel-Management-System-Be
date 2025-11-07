package com.javaweb.converter;

import org.springframework.stereotype.Component;
import com.javaweb.model.dto.ShiftDTO.ShiftResponseDTO;
import com.javaweb.model.entity.ShiftEntity;

@Component
public class ShiftConverter {

    public ShiftResponseDTO toResponseDTO(ShiftEntity entity) {
        ShiftResponseDTO dto = new ShiftResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setStartTime(entity.getStartTime().toString());
        dto.setEndTime(entity.getEndTime().toString());
        dto.setDetails(entity.getDetails());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }
}
