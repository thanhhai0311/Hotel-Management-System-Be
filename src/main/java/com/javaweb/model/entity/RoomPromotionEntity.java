package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roompromotion")
public class RoomPromotionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idRoomType", nullable = false)
    private RoomTypeEntity roomType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPromotion", nullable = false)
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
