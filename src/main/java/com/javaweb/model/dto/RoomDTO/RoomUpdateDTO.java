package com.javaweb.model.dto.RoomDTO;

import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public class RoomUpdateDTO {
	private Integer roomNumber;
    private Integer bedCount;
    private Integer maxOccupancy;
    private Float price;
    private String details;
    private Integer idHotel;
    private Integer idRoomType;
    private Integer idRoomStatus;
    
    @Nullable
    private List<MultipartFile> images;
    
//    private List<String> imageUrlsToDelete;
    
    private List<Integer> imageIdsToDelete;
    
	public Integer getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
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
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Integer getIdHotel() {
		return idHotel;
	}
	public void setIdHotel(Integer idHotel) {
		this.idHotel = idHotel;
	}
	public Integer getIdRoomType() {
		return idRoomType;
	}
	public void setIdRoomType(Integer idRoomType) {
		this.idRoomType = idRoomType;
	}
	public Integer getIdRoomStatus() {
		return idRoomStatus;
	}
	public void setIdRoomStatus(Integer idRoomStatus) {
		this.idRoomStatus = idRoomStatus;
	}
	public List<MultipartFile> getImages() {
		return images;
	}
	public void setImages(List<MultipartFile> images) {
		this.images = images;
	}
//	public List<String> getImageUrlsToDelete() {
//		return imageUrlsToDelete;
//	}
//	public void setImageUrlsToDelete(List<String> imageUrlsToDelete) {
//		this.imageUrlsToDelete = imageUrlsToDelete;
//	}
	public List<Integer> getImageIdsToDelete() {
		return imageIdsToDelete;
	}
	public void setImageIdsToDelete(List<Integer> imageIdsToDelete) {
		this.imageIdsToDelete = imageIdsToDelete;
	}

	
    
}
