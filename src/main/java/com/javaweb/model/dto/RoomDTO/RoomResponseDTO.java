package com.javaweb.model.dto.RoomDTO;

import java.util.List;

public class RoomResponseDTO {
	private Integer id;
	private int roomNumber;
	private String details;
	private String hotelName;
	private String roomStatusName;
	private List<String> imageUrls;

	// ✅ Thông tin chi tiết loại phòng
	private RoomTypeInfo roomType;

	public static class RoomTypeInfo {
		private Integer id;
		private String name;
		private String details;
		private Float price;
		private Float area;
		private Integer bedCount;
		private Integer maxOccupancy;

		// Tiện nghi (amenities)
		private Boolean isPrivateBathroom;
		private Boolean isFreeToiletries;
		private Boolean isAirConditioning;
		private Boolean isSoundproofing;
		private Boolean isTV;
		private Boolean isMiniBar;
		private Boolean isWorkDesk;
		private Boolean isSeatingArea;
		private Boolean isSafetyFeatures;
		private Boolean isSmoking;

		// Getters & Setters
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
	}

	// Getters/Setters cho RoomResponseDTO
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getRoomStatusName() {
		return roomStatusName;
	}

	public void setRoomStatusName(String roomStatusName) {
		this.roomStatusName = roomStatusName;
	}

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public RoomTypeInfo getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomTypeInfo roomType) {
		this.roomType = roomType;
	}
}
