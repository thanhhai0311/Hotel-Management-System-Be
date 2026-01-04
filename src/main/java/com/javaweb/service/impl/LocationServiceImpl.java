package com.javaweb.service.impl;

import com.javaweb.converter.LocationConverter;
import com.javaweb.model.dto.LocationDTO.LocationDTO;
import com.javaweb.model.dto.LocationDTO.LocationResponseDTO;
import com.javaweb.model.entity.HotelEntity;
import com.javaweb.model.entity.LocationEntity;
import com.javaweb.repository.HotelRepository;
import com.javaweb.repository.LocationRepository;
import com.javaweb.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private LocationConverter locationConverter;

    @Override
    public List<LocationResponseDTO> findAll() {
        List<LocationEntity> entities = locationRepository.findAll();
        List<LocationResponseDTO> dtos = new ArrayList<>();
        for (LocationEntity item : entities) {
            dtos.add(locationConverter.toDTO(item));
        }
        return dtos;
    }

    @Override
    @Transactional
    public LocationResponseDTO save(Integer id, LocationDTO locationDTO) {
        if (id == null && locationRepository.existsByName(locationDTO.getName())) {
            throw new RuntimeException("Tên địa điểm đã tồn tại!");
        }

        LocationEntity locationEntity;

        if (id != null) {
            LocationEntity oldLocation = locationRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy địa điểm!"));

            if (locationDTO.getName() != null) oldLocation.setName(locationDTO.getName());
            if (locationDTO.getDescription() != null) oldLocation.setDescription(locationDTO.getDescription());
            if (locationDTO.getThumbnail() != null) oldLocation.setThumbnail(locationDTO.getThumbnail());
            if (locationDTO.getWebsiteUrl() != null) oldLocation.setWebsiteUrl(locationDTO.getWebsiteUrl());

            locationEntity = oldLocation;
        } else {
            locationEntity = locationConverter.toEntity(locationDTO);
        }

        if (locationDTO.getHotelId() != null) {
            HotelEntity hotelEntity = hotelRepository.findById(locationDTO.getHotelId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn!"));
            locationEntity.setHotel(hotelEntity);
        } else if (locationDTO.getHotelId() == null) {
            throw new RuntimeException("Bắt buộc phải chọn khách sạn!");
        }

        locationEntity = locationRepository.save(locationEntity);
        return locationConverter.toDTO(locationEntity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!locationRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy địa điểm để xóa");
        }
        locationRepository.deleteById(id);
    }

    @Override
    public LocationResponseDTO findById(Integer id) {
        LocationEntity entity = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa điểm với ID: " + id));
        return locationConverter.toDTO(entity);
    }
}