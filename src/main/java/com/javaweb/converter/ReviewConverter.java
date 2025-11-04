package com.javaweb.converter;

import com.javaweb.model.dto.ReviewDTO.ReviewDTO;
import com.javaweb.model.dto.ReviewImageDTO.ReviewImageDTO;
import com.javaweb.model.entity.ReviewEntity;
import com.javaweb.model.entity.ReviewImageEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewConverter {

    // Entity → DTO
    public ReviewDTO toDTO(ReviewEntity entity) {
        if (entity == null) return null;

        ReviewDTO dto = new ReviewDTO();
        dto.setId(entity.getId());
        dto.setDetails(entity.getDetails());
        dto.setStar(entity.getStar());
        dto.setType(entity.getType());
        dto.setDay(entity.getDay());
        dto.setCustomerId(entity.getCustomer() != null ? entity.getCustomer().getId() : null);

        if (entity.getReviewImages() != null) {
            dto.setReviewImages(
                entity.getReviewImages().stream()
                    .map(this::toImageDTO)
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }

    // DTO → Entity
    public ReviewEntity toEntity(ReviewDTO dto) {
        if (dto == null) return null;

        ReviewEntity entity = new ReviewEntity();
        entity.setId(dto.getId());
        entity.setDetails(dto.getDetails());
        entity.setStar(dto.getStar());
        entity.setType(dto.getType());
        entity.setDay(dto.getDay());
        return entity;
    }

    // ReviewImageEntity → DTO
    public ReviewImageDTO toImageDTO(ReviewImageEntity entity) {
        if (entity == null) return null;

        ReviewImageDTO dto = new ReviewImageDTO();
        dto.setId(entity.getId());
        dto.setDetails(entity.getDetails());
        dto.setSrc(entity.getSrc());
        return dto;
    }

    // ReviewImageDTO → Entity
    public ReviewImageEntity toImageEntity(ReviewImageDTO dto) {
        if (dto == null) return null;

        ReviewImageEntity entity = new ReviewImageEntity();
        entity.setId(dto.getId());
        entity.setDetails(dto.getDetails());
        entity.setSrc(dto.getSrc());
        return entity;
    }

    // Convert danh sách
    public List<ReviewDTO> toDTOList(List<ReviewEntity> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
