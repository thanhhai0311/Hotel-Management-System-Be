package com.javaweb.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "promotion")
public class PromotionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String details;

    private String banner;

    private Float discount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean isActive;
    
    @Column(nullable = false)
    private Boolean isManuallyDisabled = false;
    
    @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoomPromotionEntity> roomPromotions;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public List<RoomPromotionEntity> getRoomPromotions() {
		return roomPromotions;
	}

	public void setRoomPromotions(List<RoomPromotionEntity> roomPromotions) {
		this.roomPromotions = roomPromotions;
	}

	public Boolean getIsManuallyDisabled() {
		return isManuallyDisabled;
	}

	public void setIsManuallyDisabled(Boolean isManuallyDisabled) {
		this.isManuallyDisabled = isManuallyDisabled;
	}
    
	
}
