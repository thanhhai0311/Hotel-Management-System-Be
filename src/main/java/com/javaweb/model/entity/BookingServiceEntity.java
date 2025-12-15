package com.javaweb.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookingservice")
public class BookingServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Float price; // giá tại thời điểm đặt --> trong trường hợp nếu khách đặt
    // mà hôm sau thay đổi giá dịch vụ thì vẫn giữ giá cũ cho khách

    private Integer quantity;

    //    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdTime;

    //    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime completedTime;

    // 0: Order (Mới gọi) | 1: Done (Đã phục vụ) | 2: Cancel (Hủy)
    private Integer status = 0;

    @ManyToOne
    @JoinColumn(name = "idBookingRoom")
    private BookingRoomEntity bookingRoom;

    @ManyToOne
    @JoinColumn(name = "idService")
    private ServiceEntity service;

    @OneToMany(mappedBy = "bookingService")
    private List<NotifacationServiceEntity> notifacationServices;

    @OneToMany(mappedBy = "bookingService")
    private List<DoingServiceEntity> doingServices;

    @PrePersist
    public void prePersist() {
        if (this.createdTime == null) this.createdTime = LocalDateTime.now();
        if (this.quantity == null) this.quantity = 1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public BookingRoomEntity getBookingRoom() {
        return bookingRoom;
    }

    public void setBookingRoom(BookingRoomEntity bookingRoom) {
        this.bookingRoom = bookingRoom;
    }

    public ServiceEntity getService() {
        return service;
    }

    public void setService(ServiceEntity service) {
        this.service = service;
    }

    public List<NotifacationServiceEntity> getNotifacationServices() {
        return notifacationServices;
    }

    public void setNotifacationServices(List<NotifacationServiceEntity> notifacationServices) {
        this.notifacationServices = notifacationServices;
    }

    public List<DoingServiceEntity> getDoingServices() {
        return doingServices;
    }

    public void setDoingServices(List<DoingServiceEntity> doingServices) {
        this.doingServices = doingServices;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCompletedTime() {
        return completedTime;
    }

    public void setCompletedTime(LocalDateTime completedTime) {
        this.completedTime = completedTime;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
