package com.javaweb.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.javaweb.model.entity.RoomEntity;

public interface RoomService {
	RoomEntity createRoom(RoomEntity room, List<MultipartFile> images) throws IOException;

	RoomEntity updateRoom(Integer id, RoomEntity roomDetails);

	void deleteRoom(Integer id);

	RoomEntity getRoomById(Integer id);

	List<RoomEntity> getAllRooms();
}
