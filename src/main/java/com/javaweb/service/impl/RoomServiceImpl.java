package com.javaweb.service.impl;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.converter.RoomConverter;
import com.javaweb.model.dto.RoomDTO.RoomCreateDTO;
import com.javaweb.model.dto.RoomDTO.RoomResponseDTO;
import com.javaweb.model.dto.RoomDTO.RoomUpdateDTO;
import com.javaweb.model.entity.*;
import com.javaweb.repository.*;
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
		if (roomRepository.existsByRoomNumberAndHotel_Id(dto.getRoomNumber(), dto.getIdHotel())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phòng số này đã tồn tại trong khách sạn!");
		}

		RoomEntity room = new RoomEntity();
		room.setRoomNumber(dto.getRoomNumber());
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

		// Upload ảnh
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

		room.setRoomImage(images);
		RoomEntity saved = roomRepository.save(room);
		return roomConverter.toResponseDTO(saved);
	}

	@Override
	public Map<String, Object> getAllRooms(Integer page, Integer size) {
		List<RoomResponseDTO> rooms;

		// Nếu không truyền page và size (hoặc < 0) -> lấy tất cả
		if (page == null || size == null || page < 0 || size < 0) {
			List<RoomEntity> allRooms = roomRepository.findAll(Sort.by("id").ascending());
			rooms = allRooms.stream().map(roomConverter::toResponseDTO).collect(Collectors.toList());

			Map<String, Object> response = new HashMap<>();
			response.put("rooms", rooms);
			response.put("totalItems", rooms.size());
			response.put("totalPages", 1);
			response.put("currentPage", 0);
			return response;
		}

		// Nếu có phân trang
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<RoomEntity> roomPage = roomRepository.findAll(pageable);

		rooms = roomPage.getContent().stream().map(roomConverter::toResponseDTO).collect(Collectors.toList());

		Map<String, Object> response = new HashMap<>();
		response.put("rooms", rooms);
		response.put("totalItems", roomPage.getTotalElements());
		response.put("totalPages", roomPage.getTotalPages());
		response.put("currentPage", roomPage.getNumber());
		return response;
	}

	@Override
	public RoomResponseDTO getRoomById(Integer id) {
		RoomEntity entity = roomRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng ID = " + id));
		return roomConverter.toResponseDTO(entity);
	}

	@Override
	public RoomResponseDTO updateRoom(Integer id, RoomUpdateDTO dto) throws IOException {
		RoomEntity room = roomRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng ID = " + id));

		if (dto.getRoomNumber() != null)
			room.setRoomNumber(dto.getRoomNumber());
		if (dto.getDetails() != null)
			room.setDetails(dto.getDetails());

		if (dto.getIdHotel() != null) {
			HotelEntity hotel = hotelRepository.findById(dto.getIdHotel())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khách sạn"));
			room.setHotel(hotel);
		}

		if (dto.getIdRoomType() != null) {
			RoomTypeEntity type = roomTypeRepository.findById(dto.getIdRoomType())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy loại phòng"));
			room.setRoomType(type);
		}

		if (dto.getIdRoomStatus() != null) {
			RoomStatusEntity status = roomStatusRepository.findById(dto.getIdRoomStatus()).orElseThrow(
					() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy trạng thái phòng"));
			room.setRoomStatus(status);
		}

		// Xoá ảnh theo ID nếu có
		if (dto.getImageIdsToDelete() != null && !dto.getImageIdsToDelete().isEmpty()) {
			for (Integer imageId : dto.getImageIdsToDelete()) {
				RoomImageEntity img = roomImageRepository.findById(imageId)
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
								"Ảnh ID = " + imageId + " không tồn tại"));
				cloudinaryService.deleteFileByUrl(img.getSrc());
				roomImageRepository.delete(img);
				room.getRoomImage().remove(img);
			}
		}

		// Upload ảnh mới
		if (dto.getImages() != null && !dto.getImages().isEmpty()) {
			for (MultipartFile file : dto.getImages()) {
				String url = cloudinaryService.uploadFile(file);
				RoomImageEntity newImg = new RoomImageEntity();
				newImg.setSrc(url);
				newImg.setRoom(room);
				roomImageRepository.save(newImg);
			}
		}

		RoomEntity updated = roomRepository.save(room);
		return roomConverter.toResponseDTO(updated);
	}

	@Override
	@Transactional
	public void deleteRoom(Integer id) {
		RoomEntity room = roomRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng ID = " + id));

		// Xoá ảnh trên Cloudinary
		if (room.getRoomImage() != null) {
			for (RoomImageEntity img : room.getRoomImage()) {
				cloudinaryService.deleteFileByUrl(img.getSrc());
				roomImageRepository.delete(img);
			}
		}

		roomRepository.delete(room);
	}

	@Override
	public Page<RoomResponseDTO> searchRooms(Integer roomNumber, Integer bedCount, Integer maxOccupancy, Float minPrice,
			Float maxPrice, Integer hotelId, Integer roomTypeId, Integer roomStatusId, String details, Integer page,
			Integer size) {

		Specification<RoomEntity> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (roomNumber != null)
				predicates.add(cb.equal(root.get("roomNumber"), roomNumber));
			if (bedCount != null)
				predicates.add(cb.equal(root.get("roomType").get("bedCount"), bedCount));
			if (maxOccupancy != null)
				predicates.add(cb.equal(root.get("roomType").get("maxOccupancy"), maxOccupancy));
			if (minPrice != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("roomType").get("price"), minPrice));
			if (maxPrice != null)
				predicates.add(cb.lessThanOrEqualTo(root.get("roomType").get("price"), maxPrice));
			if (hotelId != null)
				predicates.add(cb.equal(root.get("hotel").get("id"), hotelId));
			if (roomTypeId != null)
				predicates.add(cb.equal(root.get("roomType").get("id"), roomTypeId));
			if (roomStatusId != null)
				predicates.add(cb.equal(root.get("roomStatus").get("id"), roomStatusId));
			if (details != null && !details.trim().isEmpty())
				predicates.add(cb.like(cb.lower(root.get("details")), "%" + details.toLowerCase() + "%"));

			return cb.and(predicates.toArray(new Predicate[0]));
		};

		// Nếu không truyền page và size -> lấy tất cả
		if (page == null || size == null || page < 0 || size < 0) {
			List<RoomEntity> list = roomRepository.findAll(spec, Sort.by("id").ascending());
			List<RoomResponseDTO> dtos = list.stream().map(roomConverter::toResponseDTO).collect(Collectors.toList());
			return new PageImpl<>(dtos); // trả về Page giả (full data)
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		Page<RoomEntity> rooms = roomRepository.findAll(spec, pageable);
		return rooms.map(roomConverter::toResponseDTO);
	}

}
