package com.javaweb.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.model.dto.PromotionDTO.PromotionCreateDTO;
import com.javaweb.model.dto.PromotionDTO.PromotionResponseDTO;
import com.javaweb.model.dto.PromotionDTO.PromotionUpdateDTO;
import com.javaweb.model.entity.PromotionEntity;
import com.javaweb.repository.PromotionRepository;
import com.javaweb.service.CloudinaryService;
import com.javaweb.service.PromotionService;

@Service
public class PromotionServiceImpl implements PromotionService {

	@Autowired
	private PromotionRepository promotionRepository;
	@Autowired
	private CloudinaryService cloudinaryService;

	@Override
	public PromotionResponseDTO createPromotion(PromotionCreateDTO dto) throws IOException {
		PromotionEntity promo = new PromotionEntity();
		promo.setName(dto.getName());
		promo.setDetails(dto.getDetails());
		promo.setDiscount(dto.getDiscount());

		// Parse thời gian (vì gửi qua form-data nên là String)
		LocalDateTime start = dto.getStartTime();
		LocalDateTime end = dto.getEndTime();

		if (start != null && end != null && end.isBefore(start)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thời gian kết thúc (endTime) phải sau thời gian bắt đầu (startTime)");
		}

		promo.setStartTime(start);
		promo.setEndTime(end);

		// Upload banner nếu có
		if (dto.getBanner() != null && !dto.getBanner().isEmpty()) {
			String url = cloudinaryService.uploadFile(dto.getBanner());
			promo.setBanner(url);
		}

		// Tự động xác định trạng thái hoạt động
		if (dto.getIsActive() != null) {
			boolean expectedStatus = calculateActiveStatus(start, end);
			if (!dto.getIsActive().equals(expectedStatus)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Trạng thái isActive không phù hợp với khoảng thời gian startTime và endTime. "
								+ "Trong khoảng hợp lệ thì isActive phải là " + expectedStatus);
			}
			promo.setIsActive(dto.getIsActive());
		} else {
			LocalDateTime now = LocalDateTime.now();
			if (start != null && end != null) {
				if (now.isBefore(start)) {
					promo.setIsActive(false);
				} else if (!now.isAfter(end)) {
					promo.setIsActive(true);
				} else {
					promo.setIsActive(false);
				}
			} else {
				promo.setIsActive(false);
			}
		}

		PromotionEntity saved = promotionRepository.save(promo);
		return toDTO(saved);
	}

	@Override
	public PromotionResponseDTO updatePromotion(Integer id, PromotionUpdateDTO dto) throws IOException {
		PromotionEntity promo = promotionRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khuyến mãi ID = " + id));

		if (dto.getName() != null)
			promo.setName(dto.getName());
		if (dto.getDetails() != null)
			promo.setDetails(dto.getDetails());
		if (dto.getDiscount() != null)
			promo.setDiscount(dto.getDiscount());
//		if (dto.getStartTime() != null)
//			promo.setStartTime(dto.getStartTime());
//		if (dto.getEndTime() != null)
//			promo.setEndTime(dto.getEndTime());
//		if (dto.getIsActive() != null)
//			promo.setIsActive(dto.getIsActive());

		boolean timeChanged = false;
		LocalDateTime start = dto.getStartTime();
		LocalDateTime end = dto.getEndTime();

//		System.out.println(start);
//		System.out.println(end);

		if (start != null && end != null && end.isBefore(start)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Thời gian kết thúc (endTime) phải sau thời gian bắt đầu (startTime)");
		}

		if (dto.getStartTime() != null) {
			start = dto.getStartTime();
			promo.setStartTime(start);
			timeChanged = true;
		}
		if (dto.getEndTime() != null) {
			end = dto.getEndTime();
			promo.setEndTime(end);
			timeChanged = true;
		}

		// Xử lý xóa / cập nhật ảnh banner
		if (Boolean.TRUE.equals(dto.getDeleteBanner())) {
			// Nếu có ảnh cũ thì xóa trên Cloudinary
			if (promo.getBanner() != null && !promo.getBanner().isEmpty()) {
				try {
					cloudinaryService.deleteFileByUrl(promo.getBanner());
					promo.setBanner(null);
				} catch (Exception e) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"Lỗi khi xóa ảnh banner cũ: " + e.getMessage());
				}
			}
		}

		// Nếu có upload ảnh mới
		if (dto.getBanner() != null && !dto.getBanner().isEmpty()) {
			// Nếu đang có ảnh cũ thì xóa trước
			if (promo.getBanner() != null && !promo.getBanner().isEmpty()) {
				try {
					cloudinaryService.deleteFileByUrl(promo.getBanner());
				} catch (Exception e) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
							"Lỗi khi xóa ảnh banner cũ: " + e.getMessage());
				}
			}
			// Upload ảnh mới
			try {
				String url = cloudinaryService.uploadFile(dto.getBanner());
				promo.setBanner(url);
			} catch (IOException e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Lỗi khi upload ảnh banner mới: " + e.getMessage());
			}
		}

		if (dto.getIsActive() != null) {
			boolean expectedStatus = calculateActiveStatus(start, end);
			if (!dto.getIsActive().equals(expectedStatus)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Giá trị isActive không hợp lệ với khoảng thời gian startTime và endTime. "
								+ "Trong khoảng hợp lệ thì isActive phải là " + expectedStatus);
			}
			promo.setIsActive(dto.getIsActive());
		}
		// Nếu không gửi isActive, nhưng có thay đổi start/end → cập nhật lại tự động
		else if (timeChanged) {
			promo.setIsActive(calculateActiveStatus(start, end));
		}

		PromotionEntity updated = promotionRepository.save(promo);
		return toDTO(updated);
	}

	@Override
	public PromotionResponseDTO getPromotionById(Integer id) {
		PromotionEntity promo = promotionRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khuyến mãi ID = " + id));
		return toDTO(promo);
	}

	@Override
	public void deletePromotion(Integer id) {
		PromotionEntity promo = promotionRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khuyến mãi ID = " + id));

		if (promo.getBanner() != null) {
			try {
				cloudinaryService.deleteFileByUrl(promo.getBanner());
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Lỗi khi xoá banner: " + e.getMessage());
			}
		}

		promotionRepository.delete(promo);
	}

	@Override
	public Map<String, Object> getAllPromotions(Integer page, Integer size) {
		List<PromotionResponseDTO> promotions;
		if (page == null || size == null || page < 0 || size < 0) {
			promotions = promotionRepository.findAll(Sort.by("id").ascending()).stream().map(this::toDTO)
					.collect(Collectors.toList());
			Map<String, Object> result = new HashMap<>();
			result.put("promotions", promotions);
			result.put("totalItems", promotions.size());
			return result;
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		Page<PromotionEntity> pageResult = promotionRepository.findAll(pageable);
		promotions = pageResult.getContent().stream().map(this::toDTO).collect(Collectors.toList());

		Map<String, Object> result = new HashMap<>();
		result.put("promotions", promotions);
		result.put("currentPage", pageResult.getNumber());
		result.put("totalItems", pageResult.getTotalElements());
		result.put("totalPages", pageResult.getTotalPages());
		return result;
	}

	@Override
	public Page<PromotionResponseDTO> searchPromotions(String keyword, Boolean isActive, Integer page, Integer size) {
		Specification<PromotionEntity> spec = (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (keyword != null && !keyword.trim().isEmpty()) {
				predicates.add(cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%"));
			}
			if (isActive != null) {
				predicates.add(cb.equal(root.get("isActive"), isActive));
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};

		if (page == null || size == null || page < 0 || size < 0) {
			List<PromotionResponseDTO> list = promotionRepository.findAll(spec, Sort.by("id").ascending()).stream()
					.map(this::toDTO).collect(Collectors.toList());
			return new PageImpl<>(list);
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		Page<PromotionEntity> pages = promotionRepository.findAll(spec, pageable);
		return pages.map(this::toDTO);
	}

	@Override
	public PromotionResponseDTO updateActiveStatus(Integer id, Boolean isActive) {
		PromotionEntity promo = promotionRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khuyến mãi ID = " + id));

		LocalDateTime start = promo.getStartTime();
		LocalDateTime end = promo.getEndTime();

		// Tính trạng thái hợp lệ theo thời gian hiện tại
		boolean inTimeWindow = calculateActiveStatus(start, end); // true nếu now trong [start, end]

		// Quy tắc:
		// - Yêu cầu BẬT (true) nhưng đang ngoài khoảng thời gian -> lỗi 400
		if (Boolean.TRUE.equals(isActive) && !inTimeWindow) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Không thể bật khuyến mãi vì đang ngoài khoảng thời gian hiệu lực (startTime - endTime).");
		}

		// - Yêu cầu TẮT (false) -> luôn cho phép.
		// Nếu đang trong khoảng thời gian -> đánh dấu tắt thủ công để scheduler không
		// tự bật lại.
		promo.setIsActive(isActive);
		if (Boolean.FALSE.equals(isActive) && inTimeWindow) {
			promo.setIsManuallyDisabled(true); // admin tắt tay trong khung giờ hợp lệ
		} else if (Boolean.TRUE.equals(isActive)) {
			promo.setIsManuallyDisabled(false); // bật lại -> bỏ cờ tắt tay
		}

		PromotionEntity updated = promotionRepository.save(promo);
		return toDTO(updated);
	}

	private boolean calculateActiveStatus(LocalDateTime start, LocalDateTime end) {
		LocalDateTime now = LocalDateTime.now();
		if (start == null || end == null)
			return false;
		if (now.isBefore(start))
			return false;
		if (now.isAfter(end))
			return false;
		return true;
	}

	/**
	 * Tự động cập nhật trạng thái active dựa trên thời gian
	 */
	private void updateActiveStatus(PromotionEntity promo) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime start = promo.getStartTime();
		LocalDateTime end = promo.getEndTime();

		if (start == null || end == null) {
			promo.setIsActive(false);
		} else if (now.isBefore(start)) {
			promo.setIsActive(false);
		} else if (!now.isAfter(end)) {
			promo.setIsActive(true);
		} else {
			promo.setIsActive(false);
		}
	}

	/**
	 * Scheduler tự động chạy mỗi giờ để update trạng thái promotion
	 */
	@Scheduled(cron = "0 0 * * * *") // mỗi 1 tiếng
	public void autoUpdatePromotionStatus() {
		List<PromotionEntity> all = promotionRepository.findAll();

		for (PromotionEntity p : all) {
			// Bỏ qua nếu admin tắt thủ công
			if (Boolean.TRUE.equals(p.getIsManuallyDisabled()))
				continue;

			boolean expected = calculateActiveStatus(p.getStartTime(), p.getEndTime());
			if (!Objects.equals(p.getIsActive(), expected)) {
				p.setIsActive(expected);
				promotionRepository.save(p);
			}
		}
	}

	private PromotionResponseDTO toDTO(PromotionEntity entity) {
		PromotionResponseDTO dto = new PromotionResponseDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDetails(entity.getDetails());
		dto.setDiscount(entity.getDiscount());
		dto.setBanner(entity.getBanner());
		dto.setStartTime(entity.getStartTime());
		dto.setEndTime(entity.getEndTime());
		dto.setIsActive(entity.getIsActive());
		return dto;
	}
}
