package com.javaweb.model.dto.RoomDTO;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class RoomUpdateDTO {
	private Integer roomNumber;
	private String details;
	private Integer idHotel;
	private Integer idRoomType;
	private Integer idRoomStatus;

	// Danh sách ảnh mới upload
	private List<MultipartFile> images;

	// Danh sách ID ảnh cần xoá
	private List<Integer> imageIdsToDelete;

	// Getters & Setters
	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
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

	public List<Integer> getImageIdsToDelete() {
		return imageIdsToDelete;
	}

	public void setImageIdsToDelete(List<Integer> imageIdsToDelete) {
		this.imageIdsToDelete = imageIdsToDelete;
	}
}
