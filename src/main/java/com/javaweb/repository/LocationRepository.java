package com.javaweb.repository;

import com.javaweb.model.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {
    boolean existsByName(String name);

    List<LocationEntity> findByHotel_Id(Long hotelId);
}