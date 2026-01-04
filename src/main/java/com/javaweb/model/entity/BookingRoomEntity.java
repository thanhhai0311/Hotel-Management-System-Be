package com.javaweb.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookingroom")
public class BookingRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "contractCheckInTime", nullable = false)
    private LocalDateTime contractCheckInTime;

    @Column(name = "contractCheckOutTime", nullable = false)
    private LocalDateTime contractCheckOutTime;

    @Column(name = "actualCheckInTime", nullable = false)
    private LocalDateTime actualCheckInTime;

    @Column(name = "actualCheckOutTime", nullable = false)
    private LocalDateTime actualCheckOutTime;

    @Column(name = "status", nullable = false)
    private Integer status = 1; // 1: chưa checkout, 2: đã checkout

    @ManyToOne
    @JoinColumn(name = "idBill", nullable = false)
    private BillEntity bill;

    @ManyToOne
    @JoinColumn(name = "idCustomer", nullable = false)
    private UserEntity customer;

    @ManyToOne
    @JoinColumn(name = "idRoomPromotion", nullable = false)
    private RoomPromotionEntity roomPromotion;

    @ManyToOne
    @JoinColumn(name = "idRoom", nullable = false)
    private RoomEntity room;

    @OneToMany(mappedBy = "bookingRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingServiceEntity> bookingServices;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getContractCheckInTime() {
        return contractCheckInTime;
    }

    public void setContractCheckInTime(LocalDateTime contractCheckInTime) {
        this.contractCheckInTime = contractCheckInTime;
    }

    public LocalDateTime getContractCheckOutTime() {
        return contractCheckOutTime;
    }

    public void setContractCheckOutTime(LocalDateTime contractCheckOutTime) {
        this.contractCheckOutTime = contractCheckOutTime;
    }

    public LocalDateTime getActualCheckInTime() {
        return actualCheckInTime;
    }

    public void setActualCheckInTime(LocalDateTime actualCheckInTime) {
        this.actualCheckInTime = actualCheckInTime;
    }

    public LocalDateTime getActualCheckOutTime() {
        return actualCheckOutTime;
    }

    public void setActualCheckOutTime(LocalDateTime actualCheckOutTime) {
        this.actualCheckOutTime = actualCheckOutTime;
    }

    public UserEntity getCustomer() {
        return customer;
    }

    public void setCustomer(UserEntity customer) {
        this.customer = customer;
    }

    public RoomPromotionEntity getRoomPromotion() {
        return roomPromotion;
    }

    public void setRoomPromotion(RoomPromotionEntity roomPromotion) {
        this.roomPromotion = roomPromotion;
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setRoom(RoomEntity room) {
        this.room = room;
    }

    public List<BookingServiceEntity> getBookingServices() {
        return bookingServices;
    }

    public void setBookingServices(List<BookingServiceEntity> bookingServices) {
        this.bookingServices = bookingServices;
    }

    public BillEntity getBill() {
        return bill;
    }

    public void setBill(BillEntity bill) {
        this.bill = bill;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
