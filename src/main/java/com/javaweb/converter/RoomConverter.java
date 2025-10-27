package com.javaweb.converter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.model.dto.RoomDTO.RoomResponseDTO;
import com.javaweb.model.entity.RoomEntity;
import com.javaweb.model.entity.RoomImageEntity;
import com.javaweb.repository.RoomImageRepository;

@Component
public class RoomConverter {

    @Autowired
    private RoomImageRepository imageRepository;

    public RoomResponseDTO toResponseDTO(RoomEntity entity) {
        RoomResponseDTO dto = new RoomResponseDTO();

        dto.setId(entity.getId());
        dto.setRoomNumber(entity.getRoomNumber());
        dto.setBedCount(entity.getBedCount());
        dto.setMaxOccupancy(entity.getMaxOccupancy());
        dto.setPrice(entity.getPrice());
        dto.setDetails(entity.getDetails());

        if (entity.getHotel() != null) {
            dto.setHotelId(entity.getHotel().getId());
            dto.setHotelName(entity.getHotel().getName());
        }

        if (entity.getRoomType() != null) {
            dto.setRoomTypeId(entity.getRoomType().getId());
            dto.setRoomTypeName(entity.getRoomType().getName());
        }

        if (entity.getRoomStatus() != null) {
            dto.setRoomStatusId(entity.getRoomStatus().getId());
            dto.setRoomStatusName(entity.getRoomStatus().getName());
        }

        // Lấy danh sách ảnh
        List<RoomImageEntity> images = entity.getRoomImage();
        if (images == null || images.isEmpty()) {
            images = imageRepository.findByRoomId(entity.getId());
        }

        if (images != null && !images.isEmpty()) {
            dto.setImageUrls(
                images.stream()
                      .map(RoomImageEntity::getSrc)
                      .collect(Collectors.toList())
            );
        } else {
            // Java 8: dùng Collections.emptyList() thay cho List.of()
            dto.setImageUrls(Collections.<String>emptyList());
        }

        return dto;
    }
}
