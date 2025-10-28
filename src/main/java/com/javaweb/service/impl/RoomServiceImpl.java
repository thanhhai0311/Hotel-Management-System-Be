package com.javaweb.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

	@Override
	public RoomResponseDTO updateRoom(Integer id, RoomUpdateDTO dto) throws IOException {
		RoomEntity room = roomRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng có id = " + id));

		// Cập nhật thông tin cơ bản
		if (dto.getRoomNumber() != null)
			room.setRoomNumber(dto.getRoomNumber());
		if (dto.getBedCount() != null)
			room.setBedCount(dto.getBedCount());
		if (dto.getMaxOccupancy() != null)
			room.setMaxOccupancy(dto.getMaxOccupancy());
		if (dto.getPrice() != null)
			room.setPrice(dto.getPrice());
		if (dto.getDetails() != null && !dto.getDetails().trim().isEmpty()) {
			room.setDetails(dto.getDetails());
		}

		if (dto.getIdHotel() != null) {
			HotelEntity hotel = hotelRepository.findById(dto.getIdHotel())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
							"Không tìm thấy khách sạn id = " + dto.getIdHotel()));
			room.setHotel(hotel);
		}
		if (dto.getIdRoomType() != null) {
			RoomTypeEntity type = roomTypeRepository.findById(dto.getIdRoomType())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
							"Không tìm thấy loại phòng id = " + dto.getIdRoomType()));
			room.setRoomType(type);
		}
		if (dto.getIdRoomStatus() != null) {
			RoomStatusEntity status = roomStatusRepository.findById(dto.getIdRoomStatus())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
							"Không tìm thấy trạng thái phòng id = " + dto.getIdRoomStatus()));
			room.setRoomStatus(status);
		}

		// Xóa ảnh cũ được chỉ định
//		if (dto.getImageUrlsToDelete() != null && !dto.getImageUrlsToDelete().isEmpty()) {
//			List<RoomImageEntity> currentImages = room.getRoomImage();
//
//			List<RoomImageEntity> remainingImages = currentImages.stream()
//					.filter(img -> !dto.getImageUrlsToDelete().contains(img.getSrc())).collect(Collectors.toList());
//
//			// Xóa trên Cloudinary + DB những ảnh bị chọn xoá
//			for (RoomImageEntity img : currentImages) {
//				if (dto.getImageUrlsToDelete().contains(img.getSrc())) {
//					cloudinaryService.deleteFileByUrl(img.getSrc());
//					roomImageRepository.delete(img);
//				}
//			}
//
//			room.getRoomImage().clear();
//			room.getRoomImage().addAll(remainingImages);
//		}

		// Xóa ảnh theo ID nếu user chọn xóa
		if (dto.getImageIdsToDelete() != null && !dto.getImageIdsToDelete().isEmpty()) {
			for (Integer imageId : dto.getImageIdsToDelete()) {
				// Tìm ảnh theo ID trong DB
				RoomImageEntity image = roomImageRepository.findById(imageId)
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
								"Không tìm thấy ảnh có ID = " + imageId));

				// Kiểm tra xem ảnh có thuộc về phòng hiện tại không
				if (!image.getRoom().getId().equals(room.getId())) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"Ảnh ID = " + imageId + " không thuộc phòng ID = " + room.getId());
				}

				// Xóa khỏi Cloudinary
				try {
					cloudinaryService.deleteFileByUrl(image.getSrc());
				} catch (Exception e) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"Lỗi khi xóa ảnh Cloudinary: " + e.getMessage());
				}

				// Xóa khỏi DB
				roomImageRepository.delete(image);
				room.getRoomImage().remove(image);
			}
		}

		// Upload ảnh mới (nếu có)
		if (dto.getImages() != null && !dto.getImages().isEmpty()) {
			for (MultipartFile file : dto.getImages()) {
				if (file != null && !file.isEmpty()) {
					String url = cloudinaryService.uploadFile(file);
					RoomImageEntity newImg = new RoomImageEntity();
					newImg.setRoom(room);
					newImg.setSrc(url);
					roomImageRepository.save(newImg);
				}
			}
		}

		RoomEntity updated = roomRepository.save(room);
		return roomConverter.toResponseDTO(updated);
	}

	@Override
	@Transactional
	public void deleteRoom(Integer id) {
		// Tìm phòng trong DB
		RoomEntity room = roomRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng có ID = " + id));

		// Xóa ảnh liên quan (nếu có)
		if (room.getRoomImage() != null && !room.getRoomImage().isEmpty()) {
			for (RoomImageEntity image : room.getRoomImage()) {
				try {
					cloudinaryService.deleteFileByUrl(image.getSrc());
				} catch (Exception e) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"Lỗi khi xóa ảnh Cloudinary: " + e.getMessage());
				}
				roomImageRepository.delete(image);
			}
		}

		// Xóa phòng
		roomRepository.delete(room);
	}

	@Override
	public Page<RoomResponseDTO> searchRooms(Integer roomNumber, Integer bedCount, Integer maxOccupancy, Float minPrice,
			Float maxPrice, Integer hotelId, Integer roomTypeId, Integer roomStatusId, String details, int page,
			int size) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

		Specification<RoomEntity> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (roomNumber != null) {
				predicates.add(cb.equal(root.get("roomNumber"), roomNumber));
			}

			if (bedCount != null) {
				predicates.add(cb.equal(root.get("bedCount"), bedCount));
			}

			if (maxOccupancy != null) {
				predicates.add(cb.equal(root.get("maxOccupancy"), maxOccupancy));
			}

			if (minPrice != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
			}

			if (maxPrice != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
			}

			if (hotelId != null) {
				predicates.add(cb.equal(root.get("hotel").get("id"), hotelId));
			}

			if (roomTypeId != null) {
				predicates.add(cb.equal(root.get("roomType").get("id"), roomTypeId));
			}

			if (roomStatusId != null) {
				predicates.add(cb.equal(root.get("roomStatus").get("id"), roomStatusId));
			}

			if (details != null && !details.trim().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("details")), "%" + details.toLowerCase() + "%"));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};

		Page<RoomEntity> rooms = roomRepository.findAll(spec, pageable);
		return rooms.map(roomConverter::toResponseDTO);
	}

}
