package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "room")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "roomNumber", nullable = false)
    private int roomNumber;

    @Column(name = "details")
    private String details;

    @ManyToOne
    @JoinColumn(name = "idHotel", nullable = false)
    private HotelEntity hotel;

    @ManyToOne
    @JoinColumn(name = "idRoomStatus", nullable = false)
    private RoomStatusEntity roomStatus;

    @ManyToOne
    @JoinColumn(name = "idRoomType", nullable = false)
    private RoomTypeEntity roomType;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RoomImageEntity> roomImage = new ArrayList<>();

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
