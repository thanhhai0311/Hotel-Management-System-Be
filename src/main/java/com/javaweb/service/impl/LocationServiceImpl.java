package com.javaweb.service.impl;

import com.javaweb.converter.LocationConverter;
import com.javaweb.model.dto.LocationDTO.LocationDTO;
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
    public List<LocationDTO> findAll() {
        List<LocationEntity> entities = locationRepository.findAll();
        List<LocationDTO> dtos = new ArrayList<>();
        for (LocationEntity item : entities) {
            dtos.add(locationConverter.toDTO(item));
        }
        return dtos;
    }

    @Override
    @Transactional
    public LocationDTO save(LocationDTO locationDTO) {
        if (locationDTO.getId() == null && locationRepository.existsByName(locationDTO.getName())) {
            throw new RuntimeException("Tên địa điểm đã tồn tại!");
        }

        LocationEntity locationEntity;

        if (locationDTO.getId() != null) {
            LocationEntity oldLocation = locationRepository.findById(locationDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy địa điểm!"));

            oldLocation.setName(locationDTO.getName());
            oldLocation.setDescription(locationDTO.getDescription());
            oldLocation.setThumbnail(locationDTO.getThumbnail());
            oldLocation.setWebsiteUrl(locationDTO.getWebsiteUrl());

            locationEntity = oldLocation;
        } else {
            locationEntity = locationConverter.toEntity(locationDTO);
        }

        if (locationDTO.getHotelId() != null) {
            HotelEntity hotelEntity = hotelRepository.findById(locationDTO.getHotelId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khách sạn!"));
            locationEntity.setHotel(hotelEntity);
        } else if (locationDTO.getId() == null) {
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
    public LocationDTO findById(Integer id) {
        LocationEntity entity = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa điểm với ID: " + id));
        return locationConverter.toDTO(entity);
    }
}