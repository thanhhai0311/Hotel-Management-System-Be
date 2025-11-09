package com.javaweb.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.model.dto.ServiceCategoryDTO.ServiceCategoryRequest;
import com.javaweb.model.dto.ServiceCategoryDTO.ServiceCategoryResponse;
import com.javaweb.model.entity.ServiceCategoryEntity;
import com.javaweb.repository.ServiceCategoryRepository;
import com.javaweb.service.ServiceCategoryService;

@Service
public class ServiceCategoryServiceImpl implements ServiceCategoryService {

	@Autowired
	private ServiceCategoryRepository serviceCategoryRepository;

	@Override
	public ServiceCategoryResponse create(ServiceCategoryRequest req) {
		if (serviceCategoryRepository.existsByNameIgnoreCase(req.getName())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên category đã tồn tại");
		}
		ServiceCategoryEntity entity = new ServiceCategoryEntity();
		entity.setName(req.getName().trim());
		entity.setDetails(req.getDetails());
		ServiceCategoryEntity saved = serviceCategoryRepository.save(entity);
		return toResponse(saved);
	}

	@Override
	public ServiceCategoryResponse update(Integer id, ServiceCategoryRequest req) {
		ServiceCategoryEntity entity = serviceCategoryRepository.findById(id) 
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy category"));
				
		if(req.getName() != null) {
			serviceCategoryRepository.findByNameIgnoreCase(req.getName().trim()).ifPresent(existed -> {
				if(!Objects.equals(existed.getId(), id)) {
					throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên category đã tồn tại");
				}
			});
			entity.setName(req.getName().trim());
		}
		entity.setDetails(req.getDetails());
		
		ServiceCategoryEntity saved = serviceCategoryRepository.save(entity);
		return toResponse(saved);
	}

	@Override
	public void delete(Integer id) {
		ServiceCategoryEntity entity = serviceCategoryRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy category"));
		
		if (entity.getServices() != null && !entity.getServices().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể xóa: Loại dịch vụ đang được sử dụng");
        }
		
		serviceCategoryRepository.delete(entity);

	}

	@Override
	public ServiceCategoryResponse getById(Integer id) {
		ServiceCategoryEntity entity = serviceCategoryRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy category"));
		
		return toResponse(entity);
	}

//	@Override
//	public Page<ServiceCategoryResponse> getAll(Pageable pageable, String keyword) {
//		Page<ServiceCategoryEntity> page = ((keyword == null || keyword.trim().isEmpty()))
//                ? serviceCategoryRepository.findAll(pageable)
//                : serviceCategoryRepository.findAll(pageable)
//                  .map(e -> e); 
//        return page.map(this::toResponse);
//	}
//
//	private ServiceCategoryResponse toResponse(ServiceCategoryEntity e) {
//		return new ServiceCategoryResponse(e.getId(), e.getName(), e.getDetails());
//	}
	
	@Override
	public Page<ServiceCategoryResponse> getAll(Pageable pageable) {
	    try {
	        // 🔹 Nếu không truyền pageable hoặc không phân trang → lấy toàn bộ
	        if (pageable == null || pageable.isUnpaged()) {
	            List<ServiceCategoryEntity> list = serviceCategoryRepository.findAll(Sort.by("id").ascending());

	            List<ServiceCategoryResponse> dtos = list.stream()
	                    .map(this::toResponse)
	                    .collect(Collectors.toList());

	            // Trả về PageImpl giả để thống nhất kiểu trả về
	            return new PageImpl<>(dtos);
	        }

	        // 🔹 Nếu có pageable → phân trang bình thường
	        Page<ServiceCategoryEntity> page = serviceCategoryRepository.findAll(pageable);
	        return page.map(this::toResponse);

	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new ResponseStatusException(
	                HttpStatus.INTERNAL_SERVER_ERROR,
	                "Lỗi khi lấy danh sách loại dịch vụ: " + e.getMessage()
	        );
	    }
	}

	private ServiceCategoryResponse toResponse(ServiceCategoryEntity e) {
	    return new ServiceCategoryResponse(e.getId(), e.getName(), e.getDetails());
	}


}
