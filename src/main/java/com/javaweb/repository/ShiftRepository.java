package com.javaweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.javaweb.model.entity.ShiftEntity;

@Repository
public interface ShiftRepository extends JpaRepository<ShiftEntity, Integer>, JpaSpecificationExecutor<ShiftEntity> {
	@Query("SELECT s FROM ShiftEntity s WHERE :time BETWEEN s.startTime AND s.endTime")
	ShiftEntity findShiftByTime(@Param("time") java.time.LocalTime time);
}
