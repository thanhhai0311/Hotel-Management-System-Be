package com.javaweb.converter;

import org.springframework.stereotype.Component;

import com.javaweb.model.dto.ShiftingDTO.ShiftingResponseDTO;
import com.javaweb.model.entity.ShiftingEntity;

@Component
public class ShiftingConverter {
    public ShiftingResponseDTO toResponseDTO(ShiftingEntity entity) {
        ShiftingResponseDTO dto = new ShiftingResponseDTO();
        dto.setId(entity.getId());
        dto.setDetails(entity.getDetails());
        dto.setDay(entity.getDay());
        if (entity.getEmployee() != null) {
        	dto.setIdEmployee(entity.getEmployee().getId());
        	dto.setEmployeeName(entity.getEmployee().getName());
        }
        if (entity.getShift() != null) {
        	dto.setIdShift(entity.getShift().getId());
        	dto.setShiftName(entity.getShift().getName());
        }
        return dto;
    }
}
