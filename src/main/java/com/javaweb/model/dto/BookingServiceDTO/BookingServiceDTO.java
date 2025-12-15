package com.javaweb.model.dto.BookingServiceDTO;

import java.time.LocalDateTime;

public class BookingServiceDTO {
    private Integer id;
    private Integer bookingRoomId;
    private Integer serviceId;

    // Thông tin hiển thị cho Frontend
    private String serviceName;
    private String unit;
    private Float price;
    private Integer quantity;
    private Float totalPrice;

    private LocalDateTime createdTime;
    private Integer status;       // 0: Order, 1: Done, 2: Cancel

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookingRoomId() {
        return bookingRoomId;
    }

    public void setBookingRoomId(Integer bookingRoomId) {
        this.bookingRoomId = bookingRoomId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setcreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}