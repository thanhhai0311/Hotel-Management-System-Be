package com.javaweb.model.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roompromotion")
public class RoomPromotionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRoomType")
    private RoomTypeEntity roomType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPromotion")
    private PromotionEntity promotion;
    
    @OneToMany(mappedBy = "roomPromotion")
    private List<BookingRoomEntity> bookingRooms;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public RoomTypeEntity getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomTypeEntity roomType) {
		this.roomType = roomType;
	}

	public PromotionEntity getPromotion() {
		return promotion;
	}

	public void setPromotion(PromotionEntity promotion) {
		this.promotion = promotion;
	}

	public List<BookingRoomEntity> getBookingRooms() {
		return bookingRooms;
	}

	public void setBookingRooms(List<BookingRoomEntity> bookingRooms) {
		this.bookingRooms = bookingRooms;
	}

	
	
}
