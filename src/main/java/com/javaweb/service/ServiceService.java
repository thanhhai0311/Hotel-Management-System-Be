package com.javaweb.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.javaweb.model.dto.ServiceDTO.ServiceCreateDTO;
import com.javaweb.model.dto.ServiceDTO.ServiceResponseDTO;
import com.javaweb.model.dto.ServiceDTO.ServiceUpdateDTO;

public interface ServiceService {
	ServiceResponseDTO createService(ServiceCreateDTO dto) throws IOException;

	Map<String, Object> getAllServices(Integer page, Integer size);

	ServiceResponseDTO getServiceById(Integer id);

	ServiceResponseDTO updateService(Integer id, ServiceUpdateDTO dto) throws IOException;

	void deleteService(Integer id);

	Page<ServiceResponseDTO> searchServices(String name, String details, Float minPrice, Float maxPrice,
			Integer isAvaiable, String unit, Integer idHotel, Integer idCategory, Integer page, Integer size);
}
