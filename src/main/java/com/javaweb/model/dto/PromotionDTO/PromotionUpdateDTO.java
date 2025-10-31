package com.javaweb.model.dto.PromotionDTO;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

public class PromotionUpdateDTO {
	private String name;
	private String details;
	private Float discount;
	private Boolean deleteBanner;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime startTime;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime endTime;
	
	private Boolean isActive;
	private MultipartFile banner;

	// Getters & Setters
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

	public MultipartFile getBanner() {
		return banner;
	}

	public void setBanner(MultipartFile banner) {
		this.banner = banner;
	}

	public Boolean getDeleteBanner() {
		return deleteBanner;
	}

	public void setDeleteBanner(Boolean deleteBanner) {
		this.deleteBanner = deleteBanner;
	}
	
	
}
