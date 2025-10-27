package com.javaweb.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaweb.model.entity.HotelEntity;

@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Integer> {

	Optional<HotelEntity> findById(Integer idHotel);

}
