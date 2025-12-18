package com.javaweb.service;

import com.javaweb.model.dto.LocationDTO.LocationDTO;

import java.util.List;

public interface LocationService {
    List<LocationDTO> findAll();

    LocationDTO save(LocationDTO locationDTO);

    void delete(Integer id);

    LocationDTO findById(Integer id);
}
