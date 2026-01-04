package com.javaweb.converter;

import com.javaweb.model.dto.LocationDTO.LocationDTO;
import com.javaweb.model.dto.LocationDTO.LocationResponseDTO;
import com.javaweb.model.entity.LocationEntity;
import org.springframework.stereotype.Component;

@Component
public class LocationConverter {

    public LocationEntity toEntity(LocationDTO dto) {
        LocationEntity entity = new LocationEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setThumbnail(dto.getThumbnail());
        entity.setWebsiteUrl(dto.getWebsiteUrl());
        return entity;
    }

    public LocationResponseDTO toDTO(LocationEntity entity) {
        LocationResponseDTO dto = new LocationResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setThumbnail(entity.getThumbnail());
        dto.setWebsiteUrl(entity.getWebsiteUrl());

        if (entity.getHotel() != null) {
            dto.setHotelId(entity.getHotel().getId());
        }
        return dto;
    }
}