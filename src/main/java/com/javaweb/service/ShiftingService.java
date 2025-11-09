package com.javaweb.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;

import com.javaweb.model.dto.ShiftingDTO.ShiftingCreateDTO;
import com.javaweb.model.dto.ShiftingDTO.ShiftingResponseDTO;
import com.javaweb.model.dto.ShiftingDTO.ShiftingUpdateDTO;

public interface ShiftingService {
    ShiftingResponseDTO create(ShiftingCreateDTO dto);
    ShiftingResponseDTO update(Integer id, ShiftingUpdateDTO dto);
    void delete(Integer id);
    ShiftingResponseDTO getById(Integer id);
    Page<ShiftingResponseDTO> search(Integer idEmployee, Integer idShift, LocalDate day, int page, int size);
    List<ShiftingResponseDTO> searchAll(Integer idEmployee, Integer idShift, LocalDate day);
    List<ShiftingResponseDTO> getAll();
    Page<ShiftingResponseDTO> getAllPaged(int page, int size);
    List<ShiftingResponseDTO> searchByDatetime(LocalDateTime datetime);
}
