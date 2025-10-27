package com.javaweb.service;

import java.io.IOException;
import java.util.Map;

import com.javaweb.model.dto.RoomDTO.RoomCreateDTO;
import com.javaweb.model.dto.RoomDTO.RoomResponseDTO;

public interface RoomService {
    RoomResponseDTO createRoom(RoomCreateDTO dto) throws IOException;
    Map<String, Object> getAllRooms(int page, int size);
    RoomResponseDTO getRoomById(Integer id);
}
