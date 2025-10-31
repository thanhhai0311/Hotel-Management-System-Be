package com.javaweb.model.dto.RoomTypeDTO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class RoomTypeCreateDTO {
	private String name;
	private String details;
	private Integer bedCount;
	private Integer maxOccupancy;
	private Float price;
	private Float area;

	private Boolean isPrivateBathroom = false;
	private Boolean isFreeToiletries = false;
	private Boolean isAirConditioning = false;
	private Boolean isSoundproofing = false;
	private Boolean isTV = false;
	private Boolean isMiniBar = false;
	private Boolean isWorkDesk = false;
	private Boolean isSeatingArea = false;
	private Boolean isSafetyFeatures = false;
	private Boolean isSmoking = false;
	
	private List<MultipartFile> images;

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

	public Integer getBedCount() {
		return bedCount;
	}

	public void setBedCount(Integer bedCount) {
		this.bedCount = bedCount;
	}

	public Integer getMaxOccupancy() {
		return maxOccupancy;
	}

	public void setMaxOccupancy(Integer maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getArea() {
		return area;
	}

	public void setArea(Float area) {
		this.area = area;
	}

	public Boolean getIsPrivateBathroom() {
		return isPrivateBathroom;
	}

	public void setIsPrivateBathroom(Boolean isPrivateBathroom) {
		this.isPrivateBathroom = isPrivateBathroom;
	}

	public Boolean getIsFreeToiletries() {
		return isFreeToiletries;
	}

	public void setIsFreeToiletries(Boolean isFreeToiletries) {
		this.isFreeToiletries = isFreeToiletries;
	}

	public Boolean getIsAirConditioning() {
		return isAirConditioning;
	}

	public void setIsAirConditioning(Boolean isAirConditioning) {
		this.isAirConditioning = isAirConditioning;
	}

	public Boolean getIsSoundproofing() {
		return isSoundproofing;
	}

	public void setIsSoundproofing(Boolean isSoundproofing) {
		this.isSoundproofing = isSoundproofing;
	}

	public Boolean getIsTV() {
		return isTV;
	}

	public void setIsTV(Boolean isTV) {
		this.isTV = isTV;
	}

	public Boolean getIsMiniBar() {
		return isMiniBar;
	}

	public void setIsMiniBar(Boolean isMiniBar) {
		this.isMiniBar = isMiniBar;
	}

	public Boolean getIsWorkDesk() {
		return isWorkDesk;
	}

	public void setIsWorkDesk(Boolean isWorkDesk) {
		this.isWorkDesk = isWorkDesk;
	}

	public Boolean getIsSeatingArea() {
		return isSeatingArea;
	}

	public void setIsSeatingArea(Boolean isSeatingArea) {
		this.isSeatingArea = isSeatingArea;
	}

	public Boolean getIsSafetyFeatures() {
		return isSafetyFeatures;
	}

	public void setIsSafetyFeatures(Boolean isSafetyFeatures) {
		this.isSafetyFeatures = isSafetyFeatures;
	}

	public Boolean getIsSmoking() {
		return isSmoking;
	}

	public void setIsSmoking(Boolean isSmoking) {
		this.isSmoking = isSmoking;
	}

	public List<MultipartFile> getImages() {
		return images;
	}

	public void setImages(List<MultipartFile> images) {
		this.images = images;
	}
	
	
}
