package com.javaweb.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.converter.RoomConverter;
import com.javaweb.model.dto.RoomDTO.RoomCreateDTO;
import com.javaweb.model.dto.RoomDTO.RoomResponseDTO;
import com.javaweb.model.entity.HotelEntity;
import com.javaweb.model.entity.RoomEntity;
import com.javaweb.model.entity.RoomImageEntity;
import com.javaweb.model.entity.RoomStatusEntity;
import com.javaweb.model.entity.RoomTypeEntity;
import com.javaweb.repository.HotelRepository;
import com.javaweb.repository.RoomImageRepository;
import com.javaweb.repository.RoomRepository;
import com.javaweb.repository.RoomStatusRepository;
import com.javaweb.repository.RoomTypeRepository;
import com.javaweb.service.CloudinaryService;
import com.javaweb.service.RoomService;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private RoomTypeRepository roomTypeRepository;

	@Autowired
	private RoomStatusRepository roomStatusRepository;

	@Autowired
	private RoomImageRepository roomImageRepository;

	@Autowired
	private CloudinaryService cloudinaryService;

	@Autowired
	private RoomConverter roomConverter;

	@Override
	public RoomResponseDTO createRoom(RoomCreateDTO dto) throws IOException {
		RoomEntity room = new RoomEntity();
		room.setRoomNumber(dto.getRoomNumber());
		room.setBedCount(dto.getBedCount());
		room.setMaxOccupancy(dto.getMaxOccupancy());
		room.setPrice(dto.getPrice());
		room.setDetails(dto.getDetails());

		HotelEntity hotel = hotelRepository.findById(dto.getIdHotel())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khách sạn"));
		RoomTypeEntity type = roomTypeRepository.findById(dto.getIdRoomType())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy loại phòng"));
		RoomStatusEntity status = roomStatusRepository.findById(dto.getIdRoomStatus()).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy trạng thái phòng"));

		room.setHotel(hotel);
		room.setRoomType(type);
		room.setRoomStatus(status);

		// Khởi tạo danh sách ảnh
		List<RoomImageEntity> images = new ArrayList<>();

		if (dto.getImages() != null && !dto.getImages().isEmpty()) {
			for (MultipartFile file : dto.getImages()) {
				String url = cloudinaryService.uploadFile(file);
				RoomImageEntity img = new RoomImageEntity();
				img.setSrc(url);
				img.setRoom(room);
				images.add(img);
			}
		}

		// Gán list ảnh cho phòng trước khi lưu
		room.setRoomImage(images);
		RoomEntity saved = roomRepository.save(room);

		// Giờ entity đã có danh sách ảnh trong bộ nhớ
		return roomConverter.toResponseDTO(saved);
	}

	@Override
	public Map<String, Object> getAllRooms(int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<RoomEntity> roomPage = roomRepository.findAll(pageable);

		List<RoomResponseDTO> rooms = roomPage.getContent().stream().map(roomConverter::toResponseDTO)
				.collect(Collectors.toList());

		Map<String, Object> response = new HashMap<>();
		response.put("rooms", rooms);
		response.put("currentPage", roomPage.getNumber());
		response.put("totalItems", roomPage.getTotalElements());
		response.put("totalPages", roomPage.getTotalPages());

		return response;
	}

	@Override
	public RoomResponseDTO getRoomById(Integer id) {
		RoomEntity room = roomRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng."));
//	    if (room == null) {
//	        return null;
//	    }
		return roomConverter.toResponseDTO(room);
	}
}
