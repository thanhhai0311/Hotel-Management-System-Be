package com.javaweb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.javaweb.model.entity.RoomPromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

@Repository
public interface RoomPromotionRepository extends JpaRepository<RoomPromotionEntity, Integer>, JpaSpecificationExecutor<RoomPromotionEntity> {

    // có phân trang
    Page<RoomPromotionEntity> findByIsActiveTrue(Pageable pageable);
    Page<RoomPromotionEntity> findByStartTimeAfter(Date now, Pageable pageable);
    Page<RoomPromotionEntity> findByEndTimeBefore(Date now, Pageable pageable);

    // không phân trang
    List<RoomPromotionEntity> findByIsActiveTrue();
    List<RoomPromotionEntity> findByStartTimeAfter(Date now);
    List<RoomPromotionEntity> findByEndTimeBefore(Date now);
}
