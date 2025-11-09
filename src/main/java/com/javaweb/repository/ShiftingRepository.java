package com.javaweb.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.javaweb.model.entity.ShiftEntity;
import com.javaweb.model.entity.ShiftingEntity;

public interface ShiftingRepository extends JpaRepository<ShiftingEntity, Integer>, JpaSpecificationExecutor<ShiftingEntity> {
	List<ShiftingEntity> findAllByDayAndShift(LocalDate day, ShiftEntity shift);
	boolean existsByEmployee_IdAndDay(Integer idEmployee, LocalDate day);
}
