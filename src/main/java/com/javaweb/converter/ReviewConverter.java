package com.javaweb.converter;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.javaweb.model.dto.ReviewDTO.ReviewResponseDTO;
import com.javaweb.model.entity.ReviewEntity;

@Component
public class ReviewConverter {
    public ReviewResponseDTO toResponseDTO(ReviewEntity entity) {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(entity.getId());
        dto.setDetails(entity.getDetails());
        dto.setStar(entity.getStar());
        dto.setType(entity.getType());
        dto.setDay(entity.getDay());
        dto.setCustomerName(entity.getCustomer() != null ? entity.getCustomer().getName() : null);

        if (entity.getReviewImages() != null) {
            dto.setImageUrls(entity.getReviewImages().stream()
                    .map(img -> img.getSrc())
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
