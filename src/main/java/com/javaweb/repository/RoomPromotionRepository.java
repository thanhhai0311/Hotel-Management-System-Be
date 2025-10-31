package com.javaweb.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.javaweb.model.entity.RoomPromotionEntity;

@Repository
public interface RoomPromotionRepository extends JpaRepository<RoomPromotionEntity, Integer>, JpaSpecificationExecutor<RoomPromotionEntity> {
    List<RoomPromotionEntity> findByPromotion_Id(Integer promotionId);
    List<RoomPromotionEntity> findByRoomType_Id(Integer roomTypeId);
}
