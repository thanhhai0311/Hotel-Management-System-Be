package com.javaweb.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookingservice")
public class BookingServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "price", nullable = false)
    private Float price; // giá tại thời điểm đặt --> trong trường hợp nếu khách đặt
    // mà hôm sau thay đổi giá dịch vụ thì vẫn giữ giá cũ cho khách

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    //    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdTime;

    //    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime completedTime;

    // 0: Order (Mới gọi) | 1: Done (Đã phục vụ) | 2: Cancel (Hủy)
    @Column(name = "status", nullable = false)
    private Integer status = 0;

    @ManyToOne
    @JoinColumn(name = "idBookingRoom", nullable = false)
    private BookingRoomEntity bookingRoom;

    @ManyToOne
    @JoinColumn(name = "idService", nullable = false)
    private ServiceEntity service;

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
