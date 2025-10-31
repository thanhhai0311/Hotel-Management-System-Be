package com.javaweb.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.javaweb.model.dto.RoomDTO.RoomCreateDTO;
import com.javaweb.model.dto.RoomDTO.RoomResponseDTO;
import com.javaweb.model.dto.RoomDTO.RoomUpdateDTO;

public interface RoomService {
    RoomResponseDTO createRoom(RoomCreateDTO dto) throws IOException;
    Map<String, Object> getAllRooms(Integer page, Integer size);
    RoomResponseDTO getRoomById(Integer id);
    RoomResponseDTO updateRoom(Integer id, RoomUpdateDTO dto) throws IOException;
    void deleteRoom(Integer id);

    Page<RoomResponseDTO> searchRooms(
        Integer roomNumber,
        Integer bedCount,
        Integer maxOccupancy,
        Float minPrice,
        Float maxPrice,
        Integer hotelId,
        Integer roomTypeId,
        Integer roomStatusId,
        String details,
        Integer page,
        Integer size
    );
}
