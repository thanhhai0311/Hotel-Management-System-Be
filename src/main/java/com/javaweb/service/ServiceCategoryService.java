package com.javaweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.javaweb.model.dto.ServiceCategoryDTO.ServiceCategoryRequest;
import com.javaweb.model.dto.ServiceCategoryDTO.ServiceCategoryResponse;

public interface ServiceCategoryService {
	ServiceCategoryResponse create(ServiceCategoryRequest req);
	ServiceCategoryResponse update(Integer id, ServiceCategoryRequest req);
    void delete(Integer id);
    ServiceCategoryResponse getById(Integer id);
    Page<ServiceCategoryResponse> getAll(Pageable pageable, String keyword);
}
