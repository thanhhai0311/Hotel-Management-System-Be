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

    private LocalDateTime contractCheckInTime;

    private LocalDateTime contractCheckOutTime;

    private LocalDateTime actualCheckInTime;

    private LocalDateTime actualCheckOutTime;

    @Column(name = "status")
    private Integer status = 1; // 1: chưa checkout, 2: đã checkout

    @ManyToOne
    @JoinColumn(name = "idBill")
    private BillEntity bill;

    @ManyToOne
    @JoinColumn(name = "idCustomer")
    private UserEntity customer;

    @ManyToOne
    @JoinColumn(name = "idRoomPromotion")
    private RoomPromotionEntity roomPromotion;

    @ManyToOne
    @JoinColumn(name = "idRoom")
    private RoomEntity room;

    @ManyToOne
    @JoinColumn(name = "idEmployee")
    private UserEntity employee;

    @OneToMany(mappedBy = "bookingRoom")
    private List<BookingServiceEntity> bookingServices;

    @OneToMany(mappedBy = "bookingRoom")
    private List<NotifacationRoomEntity> notifacationRooms;

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

    public UserEntity getEmployee() {
        return employee;
    }

    public void setEmployee(UserEntity employee) {
        this.employee = employee;
    }

    public List<BookingServiceEntity> getBookingServices() {
        return bookingServices;
    }

    public void setBookingServices(List<BookingServiceEntity> bookingServices) {
        this.bookingServices = bookingServices;
    }

    public List<NotifacationRoomEntity> getNotifacationRooms() {
        return notifacationRooms;
    }

    public void setNotifacationRooms(List<NotifacationRoomEntity> notifacationRooms) {
        this.notifacationRooms = notifacationRooms;
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
