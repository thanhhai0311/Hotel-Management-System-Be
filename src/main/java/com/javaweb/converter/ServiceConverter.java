package com.javaweb.converter;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.javaweb.model.dto.ServiceDTO.ServiceResponseDTO;
import com.javaweb.model.entity.ServiceEntity;
import com.javaweb.model.entity.ServiceImageEntity;

@Component
public class ServiceConverter {

    public ServiceResponseDTO toResponseDTO(ServiceEntity entity) {
        ServiceResponseDTO dto = new ServiceResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDetails(entity.getDetails());
        dto.setPrice(entity.getPrice());
        dto.setIsAvaiable(entity.getIsAvaiable());
        dto.setUnit(entity.getUnit());
//        dto.setQuantity(entity.getQuantity());

        if (entity.getHotel() != null) {
            dto.setHotelId(entity.getHotel().getId());
            dto.setHotelName(entity.getHotel().getName());
        }

        if (entity.getServiceCategory() != null) {
            dto.setCategoryId(entity.getServiceCategory().getId());
            dto.setCategoryName(entity.getServiceCategory().getName());
        }

        if (entity.getServiceImages() != null) {
            List<String> urls = entity.getServiceImages()
                    .stream()
                    .map(ServiceImageEntity::getSrc)
                    .collect(Collectors.toList());
            dto.setImageUrls(urls);
        }

        return dto;
    }
}
