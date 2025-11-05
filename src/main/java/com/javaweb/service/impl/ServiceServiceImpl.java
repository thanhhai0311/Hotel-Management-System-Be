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

import com.javaweb.converter.ServiceConverter;
import com.javaweb.model.dto.ServiceCategoryDTO.ServiceUpdateDTO;
import com.javaweb.model.dto.ServiceDTO.ServiceCreateDTO;
import com.javaweb.model.dto.ServiceDTO.ServiceResponseDTO;
import com.javaweb.model.entity.HotelEntity;
import com.javaweb.model.entity.ServiceCategoryEntity;
import com.javaweb.model.entity.ServiceEntity;
import com.javaweb.model.entity.ServiceImageEntity;
import com.javaweb.repository.HotelRepository;
import com.javaweb.repository.ServiceCategoryRepository;
import com.javaweb.repository.ServiceImageRepository;
import com.javaweb.repository.ServiceRepository;
import com.javaweb.service.CloudinaryService;
import com.javaweb.service.ServiceService;

@Service
public class ServiceServiceImpl implements ServiceService {

	@Autowired
	private ServiceRepository serviceRepository;

	@Autowired
	private ServiceCategoryRepository serviceCategoryRepository;

	@Autowired
	private ServiceImageRepository serviceImageRepository;

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private CloudinaryService cloudinaryService;

	@Autowired
	private ServiceConverter serviceConverter;

	@Override
	public ServiceResponseDTO createService(ServiceCreateDTO dto) throws IOException {
		try {
			// Khởi tạo entity
			ServiceEntity service = new ServiceEntity();
			service.setName(dto.getName());
			service.setDetails(dto.getDetails());
			service.setPrice(dto.getPrice());
			service.setIsAvaiable(dto.getIsAvaiable());
			service.setUnit(dto.getUnit());
			service.setQuantity(dto.getQuantity());

			// Liên kết hotel
			HotelEntity hotel = hotelRepository.findById(dto.getIdHotel())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khách sạn"));
			service.setHotel(hotel);

			// Liên kết category
			ServiceCategoryEntity category = serviceCategoryRepository.findById(dto.getIdCategory()).orElseThrow(
					() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy loại dịch vụ"));
			service.setServiceCategory(category);

			// Upload ảnh lên Cloudinary
			List<ServiceImageEntity> images = new ArrayList<>();
			if (dto.getImages() != null && !dto.getImages().isEmpty()) {
				for (MultipartFile file : dto.getImages()) {
					if (file != null && !file.isEmpty()) {
						String url = cloudinaryService.uploadFile(file);
						ServiceImageEntity img = new ServiceImageEntity();
						img.setSrc(url);
						img.setService(service);
						img.setDetails("Ảnh cho service " + img.getService().getName());
						images.add(img);
					}
				}
			}

			service.setServiceImages(images);

			// Lưu DB
			ServiceEntity saved = serviceRepository.save(service);

			return serviceConverter.toResponseDTO(saved);

		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Lỗi khi tạo dịch vụ: " + e.getMessage());
		}
	}

	@Override
	public Map<String, Object> getAllServices(int page, int size) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			Page<ServiceEntity> servicePage = serviceRepository.findAll(pageable);

			List<ServiceResponseDTO> services = servicePage.getContent().stream().map(serviceConverter::toResponseDTO)
					.collect(Collectors.toList());

			Map<String, Object> response = new HashMap<>();
			response.put("services", services);
			response.put("currentPage", servicePage.getNumber());
			response.put("totalItems", servicePage.getTotalElements());
			response.put("totalPages", servicePage.getTotalPages());

			return response;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Lỗi khi lấy danh sách dịch vụ: " + e.getMessage());
		}
	}

	@Override
	public ServiceResponseDTO getServiceById(Integer id) {
		try {
			ServiceEntity service = serviceRepository.findById(id).orElseThrow(
					() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy dịch vụ có ID = " + id));
			return serviceConverter.toResponseDTO(service);
		} catch (ResponseStatusException e) {
			throw e; // ném lại để controller xử lý
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Lỗi khi lấy thông tin dịch vụ: " + e.getMessage());
		}
	}

	@Override
	public ServiceResponseDTO updateService(Integer id, ServiceUpdateDTO dto) throws IOException {
		try {
			ServiceEntity service = serviceRepository.findById(id).orElseThrow(
					() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy dịch vụ có ID = " + id));

			// Cập nhật thông tin cơ bản
			if (dto.getName() != null)
				service.setName(dto.getName());
			if (dto.getDetails() != null)
				service.setDetails(dto.getDetails());
			if (dto.getPrice() != null)
				service.setPrice(dto.getPrice());
			if (dto.getIsAvaiable() != null)
				service.setIsAvaiable(dto.getIsAvaiable());
			if (dto.getUnit() != null)
				service.setUnit(dto.getUnit());
			if (dto.getQuantity() != null)
				service.setQuantity(dto.getQuantity());

			// Cập nhật quan hệ Hotel
			if (dto.getIdHotel() != null) {
				HotelEntity hotel = hotelRepository.findById(dto.getIdHotel())
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
								"Không tìm thấy khách sạn id = " + dto.getIdHotel()));
				service.setHotel(hotel);
			}

			// Cập nhật quan hệ Category
			if (dto.getIdCategory() != null) {
				ServiceCategoryEntity category = serviceCategoryRepository.findById(dto.getIdCategory())
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
								"Không tìm thấy loại dịch vụ id = " + dto.getIdCategory()));
				service.setServiceCategory(category);
			}

			// Xóa ảnh theo ID nếu người dùng chọn xóa
			if (dto.getImageIdsToDelete() != null && !dto.getImageIdsToDelete().isEmpty()) {
				for (Integer imageId : dto.getImageIdsToDelete()) {
					ServiceImageEntity image = serviceImageRepository.findById(imageId)
							.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
									"Không tìm thấy ảnh có ID = " + imageId));

					// kiểm tra ảnh có thuộc về service này không
					if (!image.getService().getId().equals(service.getId())) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
								"Ảnh ID = " + imageId + " không thuộc dịch vụ ID = " + service.getId());
					}

					// xóa khỏi Cloudinary
					try {
						cloudinaryService.deleteFileByUrl(image.getSrc());
					} catch (Exception e) {
						throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
								"Lỗi khi xóa ảnh Cloudinary: " + e.getMessage());
					}

					// xóa khỏi DB
					serviceImageRepository.delete(image);
					service.getServiceImages().remove(image);
				}
			}

			// Upload ảnh mới (nếu có)
			if (dto.getImages() != null && !dto.getImages().isEmpty()) {
				for (MultipartFile file : dto.getImages()) {
					if (file != null && !file.isEmpty()) {
						String url = cloudinaryService.uploadFile(file);
						ServiceImageEntity newImg = new ServiceImageEntity();
						newImg.setSrc(url);
						newImg.setService(service);
						serviceImageRepository.save(newImg);
						service.getServiceImages().add(newImg);
					}
				}
			}

			ServiceEntity updated = serviceRepository.save(service);
			return serviceConverter.toResponseDTO(updated);

		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Lỗi khi cập nhật dịch vụ: " + e.getMessage());
		}
	}

	@Override
	@Transactional
	public void deleteService(Integer id) {
		try {
			// Tìm dịch vụ theo ID
			ServiceEntity service = serviceRepository.findById(id).orElseThrow(
					() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy dịch vụ có ID = " + id));

			// Xóa ảnh liên quan nếu có
			if (service.getServiceImages() != null && !service.getServiceImages().isEmpty()) {
				for (ServiceImageEntity image : service.getServiceImages()) {
					try {
						cloudinaryService.deleteFileByUrl(image.getSrc());
					} catch (Exception e) {
						throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
								"Lỗi khi xóa ảnh Cloudinary: " + e.getMessage());
					}
					serviceImageRepository.delete(image);
				}
			}

			// Xóa dịch vụ
			serviceRepository.delete(service);

		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Lỗi khi xóa dịch vụ: " + e.getMessage());
		}
	}

	@Override
	public Page<ServiceResponseDTO> searchServices(String name, String details, Float minPrice, Float maxPrice,
			Integer isAvaiable, String unit, Integer quantity, Integer idHotel, Integer idCategory, int page,
			int size) {

		try {
			Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

			Specification<ServiceEntity> spec = (root, query, cb) -> {
				List<Predicate> predicates = new ArrayList<>();

				if (name != null && !name.trim().isEmpty()) {
					predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
				}

				if (details != null && !details.trim().isEmpty()) {
					predicates.add(cb.like(cb.lower(root.get("details")), "%" + details.toLowerCase() + "%"));
				}

				if (minPrice != null) {
					predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
				}

				if (maxPrice != null) {
					predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
				}

				if (isAvaiable != null) {
					predicates.add(cb.equal(root.get("isAvaiable"), isAvaiable));
				}

				if (unit != null && !unit.trim().isEmpty()) {
					predicates.add(cb.like(cb.lower(root.get("unit")), "%" + unit.toLowerCase() + "%"));
				}

				if (quantity != null) {
					predicates.add(cb.equal(root.get("quantity"), quantity));
				}

				if (idHotel != null) {
					predicates.add(cb.equal(root.get("hotel").get("id"), idHotel));
				}

				if (idCategory != null) {
					predicates.add(cb.equal(root.get("serviceCategory").get("id"), idCategory));
				}

				return cb.and(predicates.toArray(new Predicate[0]));
			};

			Page<ServiceEntity> pageResult = serviceRepository.findAll(spec, pageable);
			return pageResult.map(serviceConverter::toResponseDTO);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Lỗi khi tìm kiếm dịch vụ: " + e.getMessage());
		}
	}

}
