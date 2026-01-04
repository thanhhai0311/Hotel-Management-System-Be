package com.javaweb.model.dto.BookingRoomDTO;

import java.time.LocalDateTime;
import java.util.List;

public class BookingResponseDTO {
    // Basic Info
    private Integer id;
    private LocalDateTime contractCheckInTime;
    private LocalDateTime contractCheckOutTime;
    private LocalDateTime actualCheckInTime;
    private LocalDateTime actualCheckOutTime;

    // Room Info
    private Integer roomId;
    private int roomNumber;
    private String roomType;
    private Double roomPrice;

    // Customer Info
    private Integer customerId;
    private String customerName;
    private String customerPhone;

    // Employee Info
//    private Integer employeeId;
//    private String employeeName;

    // Promotion
    private String promotionName;
    private Float discount;

    // Bill Info
    private Integer billId;
    private String paymentStatus;
    private Float totalMoney;

    // Services List
    private List<BookingServiceInfoDTO> services;

    // --- Inner Class: Service Detail ---
    public static class BookingServiceInfoDTO {
        private String serviceName;
        private int quantity;
        private Double price;
        private Double total;

        public BookingServiceInfoDTO(String serviceName, int quantity, Double price) {
            this.serviceName = serviceName;
            this.quantity = quantity;
            this.price = price;
            this.total = price * quantity;
        }

        // Getters & Setters inner class
        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Double getTotal() {
            return total;
        }

        public void setTotal(Double total) {
            this.total = total;
        }
    }

    // --- Getters & Setters Main Class ---
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

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomName) {
        this.roomNumber = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Double getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(Double roomPrice) {
        this.roomPrice = roomPrice;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

//    public Integer getEmployeeId() {
//        return employeeId;
//    }
//
//    public void setEmployeeId(Integer employeeId) {
//        this.employeeId = employeeId;
//    }
//
//    public String getEmployeeName() {
//        return employeeName;
//    }
//
//    public void setEmployeeName(String employeeName) {
//        this.employeeName = employeeName;
//    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public Float getDiscount() {
        return discount;
    }

    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public List<BookingServiceInfoDTO> getServices() {
        return services;
    }

    public void setServices(List<BookingServiceInfoDTO> services) {
        this.services = services;
    }
}