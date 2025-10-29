package com.javaweb.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.converter.RoomPromotionConverter;
import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionCreateDTO;
import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionResponseDTO;
import com.javaweb.model.dto.RoomPromotionDTO.RoomPromotionUpdateDTO;
import com.javaweb.model.entity.RoomEntity;
import com.javaweb.model.entity.RoomPromotionEntity;
import com.javaweb.repository.RoomPromotionRepository;
import com.javaweb.repository.RoomRepository;
import com.javaweb.service.CloudinaryService;
import com.javaweb.service.RoomPromotionService;

@Service
public class RoomPromotionServiceImpl implements RoomPromotionService {

	@Autowired
	private RoomPromotionRepository roomPromotionRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private CloudinaryService cloudinaryService;

	@Autowired
	private RoomPromotionConverter roomPromotionConverter;

	@Override
	public List<RoomPromotionResponseDTO> createRoomPromotion(RoomPromotionCreateDTO dto) throws IOException {
		if (dto.getRoomIds() == null || dto.getRoomIds().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Danh sách phòng không được để trống");
		}

		String bannerUrl = null;
		MultipartFile bannerFile = dto.getBanner();

		if (bannerFile != null && !bannerFile.isEmpty()) {
			bannerUrl = cloudinaryService.uploadFile(bannerFile);
		}

		List<RoomPromotionResponseDTO> responseList = new ArrayList<>();
		Date now = new Date();

		for (Integer roomId : dto.getRoomIds()) {
			RoomEntity room = roomRepository.findById(roomId).orElseThrow(
					() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng có ID = " + roomId));

			RoomPromotionEntity promotion = new RoomPromotionEntity();
			promotion.setName(dto.getName());
			promotion.setDetails(dto.getDetails());
			promotion.setDiscount(dto.getDiscount());
			promotion.setStartTime(dto.getStartTime());
			promotion.setEndTime(dto.getEndTime());
			promotion.setBanner(bannerUrl);
			promotion.setRoom(room);

			// Logic tự động xác định trạng thái kích hoạt
			if (dto.getStartTime() != null && dto.getStartTime().after(now)) {
				promotion.setIsActive(false); // chưa bắt đầu
			} else {
				promotion.setIsActive(true); // đã bắt đầu
			}

			RoomPromotionEntity saved = roomPromotionRepository.save(promotion);
			responseList.add(roomPromotionConverter.toResponseDTO(saved));
		}

		return responseList;
	}

	@Override
	public RoomPromotionResponseDTO togglePromotionStatus(Integer id, boolean active) {
		RoomPromotionEntity promo = roomPromotionRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khuyến mãi có ID = " + id));

		promo.setIsActive(active);
		RoomPromotionEntity updated = roomPromotionRepository.save(promo);
		return roomPromotionConverter.toResponseDTO(updated);
	}

	@Scheduled(cron = "0 0 * * * *") // mỗi giờ
	public void autoUpdatePromotionStatus() {
		Date now = new Date();
		List<RoomPromotionEntity> allPromotions = roomPromotionRepository.findAll();

		for (RoomPromotionEntity promo : allPromotions) {
			boolean changed = false;

			if (promo.getStartTime() != null && promo.getEndTime() != null) {
				if (promo.getStartTime().before(now) && promo.getEndTime().after(now)) {
					// đang trong khoảng thời gian khuyến mãi
					if (promo.getIsActive() == null || !promo.getIsActive()) {
						promo.setIsActive(true);
						changed = true;
					}
				} else if (promo.getEndTime().before(now)) {
					// đã hết hạn
					if (promo.getIsActive() == null || promo.getIsActive()) {
						promo.setIsActive(false);
						changed = true;
					}
				}
			}

			if (changed) {
				roomPromotionRepository.save(promo);
			}
		}
	}

	@Override
	public List<RoomPromotionResponseDTO> getActivePromotions(Integer page, Integer size) {
		List<RoomPromotionEntity> promotions;
		if (page == null || size == null) {
			promotions = roomPromotionRepository.findByIsActiveTrue(); // lấy tất cả
		} else {
			Pageable pageable = PageRequest.of(page, size);
			promotions = roomPromotionRepository.findByIsActiveTrue(pageable).getContent();
		}
		return promotions.stream().map(roomPromotionConverter::toResponseDTO).collect(Collectors.toList());

	}

	@Override
	public List<RoomPromotionResponseDTO> getUpcomingPromotions(Integer page, Integer size) {
		Date now = new Date();
		List<RoomPromotionEntity> promotions;
		if (page == null || size == null) {
			promotions = roomPromotionRepository.findByStartTimeAfter(now);
		} else {
			Pageable pageable = PageRequest.of(page, size);
			promotions = roomPromotionRepository.findByStartTimeAfter(now, pageable).getContent();
		}
		return promotions.stream().map(roomPromotionConverter::toResponseDTO).collect(Collectors.toList());

	}

	@Override
	public List<RoomPromotionResponseDTO> getExpiredPromotions(Integer page, Integer size) {
		Date now = new Date();
		List<RoomPromotionEntity> promotions;
		if (page == null || size == null) {
			promotions = roomPromotionRepository.findByEndTimeBefore(now);
		} else {
			Pageable pageable = PageRequest.of(page, size);
			promotions = roomPromotionRepository.findByEndTimeBefore(now, pageable).getContent();
		}
		return promotions.stream().map(roomPromotionConverter::toResponseDTO).collect(Collectors.toList());

	}

	@Override
	public Page<RoomPromotionResponseDTO> searchPromotions(
	        Integer hotelId,
	        String name,
	        Boolean isActive,
	        Boolean expired,
	        int page,
	        int size) {

	    Pageable pageable = PageRequest.of(page, size);

	    Specification<RoomPromotionEntity> spec = (root, query, cb) -> {
	        List<Predicate> predicates = new ArrayList<>();
	        Date now = new Date();

	        // 🔹 Lọc theo hotelId (qua quan hệ Room → Hotel)
	        if (hotelId != null) {
	            predicates.add(cb.equal(root.get("room").get("hotel").get("id"), hotelId));
	        }

	        // 🔹 Lọc theo tên khuyến mãi (LIKE)
	        if (name != null && !name.trim().isEmpty()) {
	            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
	        }

	        // 🔹 Lọc theo trạng thái isActive
	        if (isActive != null) {
	            predicates.add(cb.equal(root.get("isActive"), isActive));
	        }

	        // 🔹 Lọc theo expired (đã hết hạn hoặc chưa)
	        if (expired != null) {
	            if (expired) {
	                predicates.add(cb.lessThan(root.get("endTime"), now)); // đã hết hạn
	            } else {
	                predicates.add(cb.greaterThanOrEqualTo(root.get("endTime"), now)); // chưa hết hạn
	            }
	        }

	        return cb.and(predicates.toArray(new Predicate[0]));
	    };

	    Page<RoomPromotionEntity> promotions = roomPromotionRepository.findAll(spec, pageable);
	    return promotions.map(roomPromotionConverter::toResponseDTO);
	}

	@Override
	public RoomPromotionResponseDTO updateRoomPromotion(Integer id, RoomPromotionUpdateDTO dto) throws IOException {
	    RoomPromotionEntity promotion = roomPromotionRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                    "Không tìm thấy khuyến mãi có ID = " + id));

	    // Cập nhật các field nếu có truyền vào
	    if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
	        promotion.setName(dto.getName());
	    }

	    if (dto.getDetails() != null && !dto.getDetails().trim().isEmpty()) {
	        promotion.setDetails(dto.getDetails());
	    }

	    if (dto.getDiscount() != null) {
	        promotion.setDiscount(dto.getDiscount());
	    }

	    if (dto.getStartTime() != null) {
	        promotion.setStartTime(dto.getStartTime());
	    }

	    if (dto.getEndTime() != null) {
	        promotion.setEndTime(dto.getEndTime());
	    }

	    if (dto.getIsActive() != null) {
	        promotion.setIsActive(dto.getIsActive());
	    }

	    // Cập nhật phòng (nếu có thay đổi)
	    if (dto.getRoomId() != null) {
	        RoomEntity room = roomRepository.findById(dto.getRoomId())
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                        "Không tìm thấy phòng có ID = " + dto.getRoomId()));
	        promotion.setRoom(room);
	    }

	    // Thay banner (xóa ảnh cũ trên Cloudinary nếu có)
	    if (dto.getBanner() != null && !dto.getBanner().isEmpty()) {
	        try {
	            // Xóa ảnh cũ (nếu có)
	            if (promotion.getBanner() != null && !promotion.getBanner().isEmpty()) {
	                cloudinaryService.deleteFileByUrl(promotion.getBanner());
	            }

	            // Upload ảnh mới
	            String newBannerUrl = cloudinaryService.uploadFile(dto.getBanner());
	            promotion.setBanner(newBannerUrl);
	        } catch (Exception e) {
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
	                    "Lỗi khi cập nhật banner: " + e.getMessage());
	        }
	    }

	    // Tự động cập nhật isActive theo startTime
	    Date now = new Date();
	    if (promotion.getStartTime() != null) {
	        if (promotion.getStartTime().after(now)) {
	            promotion.setIsActive(false);
	        } else {
	            promotion.setIsActive(true);
	        }
	    }

	    RoomPromotionEntity updated = roomPromotionRepository.save(promotion);
	    return roomPromotionConverter.toResponseDTO(updated);
	}
	
	@Override
	public void deleteRoomPromotion(Integer id) throws IOException {
	    RoomPromotionEntity promotion = roomPromotionRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
	                    "Không tìm thấy khuyến mãi có ID = " + id));

	    // Xóa ảnh banner khỏi Cloudinary nếu có
	    if (promotion.getBanner() != null && !promotion.getBanner().isEmpty()) {
	        try {
	            cloudinaryService.deleteFileByUrl(promotion.getBanner());
	        } catch (Exception e) {
	            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
	                    "Không thể xóa banner trên Cloudinary: " + e.getMessage());
	        }
	    }

	    // Xóa bản ghi trong database
	    roomPromotionRepository.delete(promotion);
	}


}
