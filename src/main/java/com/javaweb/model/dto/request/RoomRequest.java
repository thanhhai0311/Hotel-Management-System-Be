package com.javaweb.model.dto.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class RoomRequest {
    private int roomNumber;
    private int bedCount;
    private int maxOccupancy;
    private float price;
    private String details;
    private Integer idHotel;
    private Integer idRoomStatus;
    private Integer idRoomType;
    private List<MultipartFile> images; // Danh sách ảnh

    public int getRoomNumber() {
        return roomNumber;
    }
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    public int getBedCount() {
        return bedCount;
    }
    public void setBedCount(int bedCount) {
        this.bedCount = bedCount;
    }
    public int getMaxOccupancy() {
        return maxOccupancy;
    }
    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
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
    public Integer getIdRoomStatus() {
        return idRoomStatus;
    }
    public void setIdRoomStatus(Integer idRoomStatus) {
        this.idRoomStatus = idRoomStatus;
    }
    public Integer getIdRoomType() {
        return idRoomType;
    }
    public void setIdRoomType(Integer idRoomType) {
        this.idRoomType = idRoomType;
    }
    public List<MultipartFile> getImages() {
        return images;
    }
    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }
}
