package com.javaweb.service.impl;

import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionCreateDTO;
import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionResponseDTO;
import com.javaweb.model.entity.PromotionEntity;
import com.javaweb.model.entity.RoomPromotionEntity;
import com.javaweb.model.entity.RoomTypeEntity;
import com.javaweb.repository.PromotionRepository;
import com.javaweb.repository.RoomPromotionRepository;
import com.javaweb.repository.RoomTypeRepository;
import com.javaweb.service.RoomPromotionService;

@Service
public class RoomPromotionServiceImpl implements RoomPromotionService {

    @Autowired
    private RoomPromotionRepository roomPromotionRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Override
    public RoomPromotionResponseDTO create(RoomPromotionCreateDTO dto) {
        RoomTypeEntity roomType = roomTypeRepository.findById(dto.getRoomTypeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy loại phòng"));

        PromotionEntity promotion = promotionRepository.findById(dto.getPromotionId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khuyến mãi"));

        RoomPromotionEntity entity = new RoomPromotionEntity();
        entity.setDetails(dto.getDetails());
        entity.setRoomType(roomType);
        entity.setPromotion(promotion);

        RoomPromotionEntity saved = roomPromotionRepository.save(entity);
        return toDTO(saved);
    }

    @Override
    public RoomPromotionResponseDTO update(Integer id, RoomPromotionCreateDTO dto) {
        RoomPromotionEntity entity = roomPromotionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy RoomPromotion"));

        if (dto.getDetails() != null)
            entity.setDetails(dto.getDetails());

        if (dto.getRoomTypeId() != null) {
            RoomTypeEntity roomType = roomTypeRepository.findById(dto.getRoomTypeId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy loại phòng"));
            entity.setRoomType(roomType);
        }

        if (dto.getPromotionId() != null) {
            PromotionEntity promotion = promotionRepository.findById(dto.getPromotionId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khuyến mãi"));
            entity.setPromotion(promotion);
        }

        RoomPromotionEntity updated = roomPromotionRepository.save(entity);
        return toDTO(updated);
    }

    @Override
    public void delete(Integer id) {
        RoomPromotionEntity entity = roomPromotionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy RoomPromotion"));
        roomPromotionRepository.delete(entity);
    }

    @Override
    public Map<String, Object> getAll(Integer page, Integer pageSize) {
        List<RoomPromotionResponseDTO> data;
        long totalItems;
        int totalPages = 1;
        int currentPage = 0;

        if (page != null && pageSize != null) {
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").descending());
            Page<RoomPromotionEntity> pageData = roomPromotionRepository.findAll(pageable);
            data = pageData.getContent().stream().map(this::toDTO).collect(Collectors.toList());
            totalItems = pageData.getTotalElements();
            totalPages = pageData.getTotalPages();
            currentPage = pageData.getNumber();
        } else {
            List<RoomPromotionEntity> all = roomPromotionRepository.findAll(Sort.by("id").descending());
            data = all.stream().map(this::toDTO).collect(Collectors.toList());
            totalItems = all.size();
        }

        Map<String, Object> res = new HashMap<>();
        res.put("items", data);
        res.put("currentPage", currentPage);
        res.put("totalItems", totalItems);
        res.put("totalPages", totalPages);
        return res;
    }

    @Override
    public RoomPromotionResponseDTO getById(Integer id) {
        RoomPromotionEntity entity = roomPromotionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy RoomPromotion"));
        return toDTO(entity);
    }

    @Override
    public Map<String, Object> search(Integer promotionId, Integer roomTypeId, Integer page, Integer pageSize) {
        Specification<RoomPromotionEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (promotionId != null)
                predicates.add(cb.equal(root.get("promotion").get("id"), promotionId));
            if (roomTypeId != null)
                predicates.add(cb.equal(root.get("roomType").get("id"), roomTypeId));
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        List<RoomPromotionResponseDTO> data;
        long totalItems;
        int totalPages = 1;
        int currentPage = 0;

        if (page != null && pageSize != null) {
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by("id").descending());
            Page<RoomPromotionEntity> pageData = roomPromotionRepository.findAll(spec, pageable);
            data = pageData.getContent().stream().map(this::toDTO).collect(Collectors.toList());
            totalItems = pageData.getTotalElements();
            totalPages = pageData.getTotalPages();
            currentPage = pageData.getNumber();
        } else {
            List<RoomPromotionEntity> all = roomPromotionRepository.findAll(spec, Sort.by("id").descending());
            data = all.stream().map(this::toDTO).collect(Collectors.toList());
            totalItems = all.size();
        }

        Map<String, Object> res = new HashMap<>();
        res.put("items", data);
        res.put("currentPage", currentPage);
        res.put("totalItems", totalItems);
        res.put("totalPages", totalPages);
        return res;
    }

    private RoomPromotionResponseDTO toDTO(RoomPromotionEntity e) {
        RoomPromotionResponseDTO dto = new RoomPromotionResponseDTO();
        dto.setId(e.getId());
        dto.setDetails(e.getDetails());

        if (e.getRoomType() != null) {
            dto.setRoomTypeId(e.getRoomType().getId());
            dto.setRoomTypeName(e.getRoomType().getName());
        }

        if (e.getPromotion() != null) {
            dto.setPromotionId(e.getPromotion().getId());
            dto.setPromotionName(e.getPromotion().getName());
        }

        return dto;
    }
}
