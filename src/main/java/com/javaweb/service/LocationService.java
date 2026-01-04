package com.javaweb.service;

import com.javaweb.model.dto.LocationDTO.LocationDTO;
import com.javaweb.model.dto.LocationDTO.LocationResponseDTO;

import java.util.List;

public interface LocationService {
    List<LocationResponseDTO> findAll();

    LocationResponseDTO save(Integer id, LocationDTO locationDTO);

    void delete(Integer id);

    LocationResponseDTO findById(Integer id);
}
