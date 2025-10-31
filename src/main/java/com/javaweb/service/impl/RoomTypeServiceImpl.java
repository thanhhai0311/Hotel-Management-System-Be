package com.javaweb.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
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

import com.javaweb.converter.RoomTypeConverter;
import com.javaweb.model.dto.RoomTypeDTO.RoomTypeCreateDTO;
import com.javaweb.model.dto.RoomTypeDTO.RoomTypeResponseDTO;
import com.javaweb.model.dto.RoomTypeDTO.RoomTypeUpdateDTO;
import com.javaweb.model.entity.RoomTypeEntity;
import com.javaweb.model.entity.TypeImageEntity;
import com.javaweb.repository.RoomTypeRepository;
import com.javaweb.repository.TypeImageRepository;
import com.javaweb.service.CloudinaryService;
import com.javaweb.service.RoomTypeService;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

	@Autowired
	private RoomTypeRepository roomTypeRepository;

	@Autowired
	private RoomTypeConverter roomTypeConverter;

	@Autowired
	private CloudinaryService cloudinaryService;

	@Autowired
	private TypeImageRepository typeImageRepository;

	@Override
	public RoomTypeResponseDTO createRoomType(RoomTypeCreateDTO dto) throws IOException {
		// --- Validation ---
		if (dto.getName() == null || dto.getName().trim().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên loại phòng không được để trống");
		}

		if (roomTypeRepository.existsByName(dto.getName().trim())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên loại phòng đã tồn tại");
		}

		if (dto.getPrice() == null || dto.getPrice() <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giá phòng phải lớn hơn 0");
		}

		// --- Convert & Save ---
		RoomTypeEntity entity = roomTypeConverter.toEntity(dto);

		List<TypeImageEntity> images = new ArrayList<>();

		if (dto.getImages() != null && !dto.getImages().isEmpty()) {
			for (MultipartFile file : dto.getImages()) {
				String url = cloudinaryService.uploadFile(file);

				TypeImageEntity img = new TypeImageEntity();
				img.setSrc(url);
				img.setDetails("Ảnh của loại phòng " + dto.getName());
				img.setRoomType(entity);
				images.add(img);
			}
		}

		entity.setTypeImages(images);

		RoomTypeEntity saved = roomTypeRepository.save(entity);

		// --- Convert to Response ---
		return roomTypeConverter.toResponseDTO(saved);
	}

	@Override
	public RoomTypeResponseDTO updateRoomType(Integer id, RoomTypeUpdateDTO dto) throws IOException {
		RoomTypeEntity entity = roomTypeRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy loại phòng có ID = " + id));

		// Kiểm tra trùng tên (nếu đổi tên)
		if (dto.getName() != null && !dto.getName().trim().isEmpty() && !dto.getName().trim().equals(entity.getName())
				&& roomTypeRepository.existsByName(dto.getName().trim())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên loại phòng đã tồn tại");
		}

		// Kiểm tra giá hợp lệ
		if (dto.getPrice() != null && dto.getPrice() <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Giá phòng phải lớn hơn 0");
		}

		// Cập nhật các field linh hoạt bằng converter
		roomTypeConverter.updateEntityFromDTO(entity, dto);

		// Xóa ảnh theo ID (nếu có)
		if (dto.getImageIdsToDelete() != null && !dto.getImageIdsToDelete().isEmpty()) {
			for (Integer imageId : dto.getImageIdsToDelete()) {
				TypeImageEntity image = typeImageRepository.findById(imageId)
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
								"Không tìm thấy ảnh có ID = " + imageId));

				// Kiểm tra ảnh thuộc RoomType hiện tại
				if (!image.getRoomType().getId().equals(entity.getId())) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"Ảnh ID = " + imageId + " không thuộc loại phòng ID = " + entity.getId());
				}

				try {
					cloudinaryService.deleteFileByUrl(image.getSrc());
				} catch (Exception e) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"Lỗi khi xóa ảnh Cloudinary: " + e.getMessage());
				}

				typeImageRepository.delete(image);
				entity.getTypeImages().remove(image);
			}
		}

		// Upload ảnh mới (nếu có)
		if (dto.getImages() != null && !dto.getImages().isEmpty()) {
			for (MultipartFile file : dto.getImages()) {
				if (file != null && !file.isEmpty()) {
					String url = cloudinaryService.uploadFile(file);
					TypeImageEntity newImg = new TypeImageEntity();
					newImg.setSrc(url);
					newImg.setDetails("Ảnh mới của loại phòng " + entity.getName());
					newImg.setRoomType(entity);
					typeImageRepository.save(newImg);
					entity.getTypeImages().add(newImg);
				}
			}
		}

		// Lưu và trả về DTO
		RoomTypeEntity updated = roomTypeRepository.save(entity);
		return roomTypeConverter.toResponseDTO(updated);
	}

	@Override
	public Map<String, Object> getAllRoomTypes(Integer page, Integer size) {
		Map<String, Object> response = new HashMap<>();
		if (page == null || size == null) {
			List<RoomTypeEntity> list = roomTypeRepository.findByIsDeletedFalse();
			list.sort(Comparator.comparing(RoomTypeEntity::getId));
			List<RoomTypeResponseDTO> result = list.stream().map(roomTypeConverter::toResponseDTO)
					.collect(Collectors.toList());
			response.put("roomTypes", result);
			response.put("totalItems", result.size());
			response.put("totalPages", 1);
			response.put("currentPage", 0);
			return response;
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		Page<RoomTypeEntity> pageData = roomTypeRepository.findByIsDeletedFalse(pageable);
		List<RoomTypeResponseDTO> result = pageData.getContent().stream().map(roomTypeConverter::toResponseDTO)
				.collect(Collectors.toList());
		response.put("roomTypes", result);
		response.put("currentPage", pageData.getNumber());
		response.put("totalItems", pageData.getTotalElements());
		response.put("totalPages", pageData.getTotalPages());
		return response;
	}

	@Override
	public void deleteRoomType(Integer id) {
		RoomTypeEntity roomType = roomTypeRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy loại phòng có ID = " + id));

		if (Boolean.TRUE.equals(roomType.getIsDeleted())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Loại phòng này đã bị xóa trước đó");
		}

		// Xóa ảnh khỏi Cloudinary
		if (roomType.getTypeImages() != null && !roomType.getTypeImages().isEmpty()) {
			for (TypeImageEntity img : roomType.getTypeImages()) {
				try {
					cloudinaryService.deleteFileByUrl(img.getSrc());
				} catch (Exception e) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"Lỗi khi xóa ảnh Cloudinary: " + e.getMessage());
				}
			}
		}

		// Đánh dấu đã xóa
		roomType.setIsDeleted(true);
		roomTypeRepository.save(roomType);
	}

	@Override
	public Map<String, Object> searchRoomTypes(String name, Float minPrice, Float maxPrice, Integer bedCount,
			Integer maxOccupancy, Float minArea, Float maxArea, Boolean isSmoking, Boolean isAirConditioning,
			Integer page, Integer size) {
		Map<String, Object> result = new HashMap<>();

		Specification<RoomTypeEntity> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

			// Chỉ lấy roomType chưa bị xóa
			predicates.add(cb.isFalse(root.get("isDeleted")));

			if (name != null && !name.trim().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
			}

			if (minPrice != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
			}

			if (maxPrice != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
			}

			if (bedCount != null) {
				predicates.add(cb.equal(root.get("bedCount"), bedCount));
			}

			if (maxOccupancy != null) {
				predicates.add(cb.equal(root.get("maxOccupancy"), maxOccupancy));
			}

			if (minArea != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("area"), minArea));
			}

			if (maxArea != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("area"), maxArea));
			}

			if (isSmoking != null) {
				predicates.add(cb.equal(root.get("isSmoking"), isSmoking));
			}

			if (isAirConditioning != null) {
				predicates.add(cb.equal(root.get("isAirConditioning"), isAirConditioning));
			}

			return cb.and(predicates.toArray(new Predicate[0]));
		};

		// Nếu không truyền page/size → trả về toàn bộ
		if (page == null || size == null) {
			List<RoomTypeEntity> list = roomTypeRepository.findAll(spec, Sort.by("id").ascending());
			if (list.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy loại phòng nào phù hợp");
			}

			List<RoomTypeResponseDTO> data = list.stream().map(roomTypeConverter::toResponseDTO)
					.collect(Collectors.toList());

			result.put("roomTypes", data);
			result.put("totalItems", data.size());
			result.put("totalPages", 1);
			result.put("currentPage", 0);
			return result;
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		Page<RoomTypeEntity> pageData = roomTypeRepository.findAll(spec, pageable);

		if (pageData.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy loại phòng nào phù hợp");
		}

		List<RoomTypeResponseDTO> data = pageData.getContent().stream().map(roomTypeConverter::toResponseDTO)
				.collect(Collectors.toList());

		result.put("roomTypes", data);
		result.put("totalItems", pageData.getTotalElements());
		result.put("totalPages", pageData.getTotalPages());
		result.put("currentPage", pageData.getNumber());

		return result;
	}
	
	@Override
	public RoomTypeResponseDTO getRoomTypeById(Integer id) {
	    RoomTypeEntity roomType = roomTypeRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(
	                    HttpStatus.NOT_FOUND, "Không tìm thấy loại phòng với ID: " + id));

	    // Kiểm tra soft delete (nếu có flag)
	    if (Boolean.TRUE.equals(roomType.getIsDeleted())) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loại phòng này đã bị xóa");
	    }

	    return roomTypeConverter.toResponseDTO(roomType);
	}


}
