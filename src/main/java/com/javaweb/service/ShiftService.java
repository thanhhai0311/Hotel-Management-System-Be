package com.javaweb.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import com.javaweb.model.dto.ShiftDTO.ShiftCreateDTO;
import com.javaweb.model.dto.ShiftDTO.ShiftResponseDTO;
import com.javaweb.model.dto.ShiftDTO.ShiftUpdateDTO;

public interface ShiftService {
    ShiftResponseDTO createShift(ShiftCreateDTO dto);
    ShiftResponseDTO updateShift(Integer id, ShiftUpdateDTO dto);
    void deleteShift(Integer id);
    ShiftResponseDTO getShiftById(Integer id);
    Map<String, Object> getAllShifts(Integer page, Integer size);
    Page<ShiftResponseDTO> searchShifts(String name, String details, Boolean isActive, Integer page, Integer size);
    ShiftResponseDTO updateShiftActive(Integer id, Boolean active);
    List<ShiftResponseDTO> searchAllShifts(String name, String details, Boolean isActive);
}
