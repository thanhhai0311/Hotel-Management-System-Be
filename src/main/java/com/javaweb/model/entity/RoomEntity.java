package com.javaweb.model.entity;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "Room")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "roomNumber")
    private int roomNumber;
    
    @Column(name = "bedCount")
    private int bedCount;
    
    @Column(name = "maxOccupancy")
    private int maxOccupancy;
    
    @Column(name = "price")
    private float price;
    
    @Column(name = "details")
    private String details;
    
    @ManyToOne
    @JoinColumn(name = "idHotel")
    private HotelEntity hotel;

	@ManyToOne
	@JoinColumn(name = "idRoomStatus")
	private RoomStatusEntity roomStatus;
	
	@ManyToOne
	@JoinColumn(name = "idRoomType")
	private RoomTypeEntity roomType;
	
	@OneToMany(mappedBy = "room")
    private List<RoomImageEntity> roomImage;

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

	public HotelEntity getHotel() {
		return hotel;
	}

	public void setHotel(HotelEntity hotel) {
		this.hotel = hotel;
	}

	public RoomStatusEntity getRoomStatus() {
		return roomStatus;
	}

	public void setRoomStatus(RoomStatusEntity roomStatus) {
		this.roomStatus = roomStatus;
	}

	public RoomTypeEntity getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomTypeEntity roomType) {
		this.roomType = roomType;
	}

	public List<RoomImageEntity> getRoomImage() {
		return roomImage;
	}

	public void setRoomImage(List<RoomImageEntity> roomImage) {
		this.roomImage = roomImage;
	}
	
	
}
