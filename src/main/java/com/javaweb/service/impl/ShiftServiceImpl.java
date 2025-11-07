package com.javaweb.service.impl;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.converter.ShiftConverter;
import com.javaweb.model.dto.ShiftDTO.*;
import com.javaweb.model.entity.ShiftEntity;
import com.javaweb.repository.ShiftRepository;
import com.javaweb.service.ShiftService;

@Service
public class ShiftServiceImpl implements ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private ShiftConverter shiftConverter;

    @Override
    public ShiftResponseDTO createShift(ShiftCreateDTO dto) {
        ShiftEntity entity = new ShiftEntity();
        entity.setName(dto.getName());
        entity.setStartTime(LocalTime.parse(dto.getStartTime()));
        entity.setEndTime(LocalTime.parse(dto.getEndTime()));
        entity.setDetails(dto.getDetails());
        entity.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);

        return shiftConverter.toResponseDTO(shiftRepository.save(entity));
    }

    @Override
    public ShiftResponseDTO updateShift(Integer id, ShiftUpdateDTO dto) {
        ShiftEntity entity = shiftRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ca làm có ID = " + id));

        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getStartTime() != null) entity.setStartTime(LocalTime.parse(dto.getStartTime()));
        if (dto.getEndTime() != null) entity.setEndTime(LocalTime.parse(dto.getEndTime()));
        if (dto.getDetails() != null) entity.setDetails(dto.getDetails());
        if (dto.getIsActive() != null) entity.setIsActive(dto.getIsActive());

        return shiftConverter.toResponseDTO(shiftRepository.save(entity));
    }

    @Override
    public void deleteShift(Integer id) {
        ShiftEntity entity = shiftRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ca làm có ID = " + id));
        shiftRepository.delete(entity);
    }

    @Override
    public ShiftResponseDTO getShiftById(Integer id) {
        ShiftEntity entity = shiftRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ca làm có ID = " + id));
        return shiftConverter.toResponseDTO(entity);
    }

    @Override
    public Map<String, Object> getAllShifts(Integer page, Integer size) {
        Map<String, Object> result = new HashMap<>();
        if (page == null || size == null) {
            List<ShiftEntity> all = shiftRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            List<ShiftResponseDTO> list = all.stream().map(shiftConverter::toResponseDTO).collect(Collectors.toList());
            result.put("shifts", list);
            result.put("totalItems", list.size());
            result.put("totalPages", 1);
            result.put("currentPage", 0);
            return result;
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<ShiftEntity> pages = shiftRepository.findAll(pageable);

        result.put("shifts", pages.getContent().stream().map(shiftConverter::toResponseDTO).collect(Collectors.toList()));
        result.put("currentPage", pages.getNumber());
        result.put("totalItems", pages.getTotalElements());
        result.put("totalPages", pages.getTotalPages());
        return result;
    }

    @Override
    public Page<ShiftResponseDTO> searchShifts(String name, String details, Boolean isActive, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Specification<ShiftEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (details != null && !details.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("details")), "%" + details.toLowerCase() + "%"));
            }
            if (isActive != null) {
                predicates.add(cb.equal(root.get("active"), isActive));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return shiftRepository.findAll(spec, pageable).map(shiftConverter::toResponseDTO);
    }
    
    @Override
    public List<ShiftResponseDTO> searchAllShifts(String name, String details, Boolean isActive) {
        Specification<ShiftEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (details != null && !details.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("details")), "%" + details.toLowerCase() + "%"));
            }
            if (isActive != null) {
                predicates.add(cb.equal(root.get("isActive"), isActive));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<ShiftEntity> shifts = shiftRepository.findAll(spec, Sort.by("id").ascending());
        return shifts.stream().map(shiftConverter::toResponseDTO).collect(Collectors.toList());
    }
    
    @Override
    public ShiftResponseDTO updateShiftActive(Integer id, Boolean isActive) {
        ShiftEntity entity = shiftRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ca làm có ID = " + id));
        entity.setIsActive(isActive);
        return shiftConverter.toResponseDTO(shiftRepository.save(entity));
    }

}
