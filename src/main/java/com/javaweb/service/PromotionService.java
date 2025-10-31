package com.javaweb.service;

import java.io.IOException;
import java.util.Map;
import org.springframework.data.domain.Page;
import com.javaweb.model.dto.PromotionDTO.*;

public interface PromotionService {
    PromotionResponseDTO createPromotion(PromotionCreateDTO dto) throws IOException;
    PromotionResponseDTO updatePromotion(Integer id, PromotionUpdateDTO dto) throws IOException;
    PromotionResponseDTO getPromotionById(Integer id);
    void deletePromotion(Integer id);
    Map<String, Object> getAllPromotions(Integer page, Integer size);
    Page<PromotionResponseDTO> searchPromotions(String keyword, Boolean isActive, Integer page, Integer size);
    PromotionResponseDTO updateActiveStatus(Integer id, Boolean isActive);
}
