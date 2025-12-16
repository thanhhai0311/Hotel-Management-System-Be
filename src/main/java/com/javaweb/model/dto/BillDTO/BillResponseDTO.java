package com.javaweb.model.dto.BillDTO;

import java.util.Date;
import java.util.List;

public class BillResponseDTO {
    private Integer id;
    private Date paymentDate;
    private Float totalBeforeTax;
    private Float totalAfterTax;
    private String paymentMethod;
    private String paymentStatus;

    // Thông tin khách hàng
    private Integer customerId;
    private String customerName;

    // Danh sách phòng trong hóa đơn này
    private List<BillRoomDTO> bookingRooms;

    // Getter & Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Float getTotalBeforeTax() {
        return totalBeforeTax;
    }

    public void setTotalBeforeTax(Float totalBeforeTax) {
        this.totalBeforeTax = totalBeforeTax;
    }

    public Float getTotalAfterTax() {
        return totalAfterTax;
    }

    public void setTotalAfterTax(Float totalAfterTax) {
        this.totalAfterTax = totalAfterTax;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public List<BillRoomDTO> getBookingRooms() {
        return bookingRooms;
    }

    public void setBookingRooms(List<BillRoomDTO> bookingRooms) {
        this.bookingRooms = bookingRooms;
    }

    // ==========================================
    // INNER CLASS 1: Thông tin Phòng trong Bill
    // ==========================================
    public static class BillRoomDTO {
        private Integer bookingRoomId;
        private Integer roomNumber; // Số phòng
        private String roomType;
        private Float roomPrice; // Giá phòng 1 đêm
        private String checkIn;
        private String checkOut;

        // Danh sách dịch vụ của phòng này
        private List<BillServiceDTO> services;

        // Getter & Setter
        public Integer getBookingRoomId() {
            return bookingRoomId;
        }

        public void setBookingRoomId(Integer bookingRoomId) {
            this.bookingRoomId = bookingRoomId;
        }

        public Integer getRoomNumber() {
            return roomNumber;
        }

        public void setRoomNumber(Integer roomNumber) {
            this.roomNumber = roomNumber;
        }

        public String getRoomType() {
            return roomType;
        }

        public void setRoomType(String roomType) {
            this.roomType = roomType;
        }

        public Float getRoomPrice() {
            return roomPrice;
        }

        public void setRoomPrice(Float roomPrice) {
            this.roomPrice = roomPrice;
        }

        public String getCheckIn() {
            return checkIn;
        }

        public void setCheckIn(String checkIn) {
            this.checkIn = checkIn;
        }

        public String getCheckOut() {
            return checkOut;
        }

        public void setCheckOut(String checkOut) {
            this.checkOut = checkOut;
        }

        public List<BillServiceDTO> getServices() {
            return services;
        }

        public void setServices(List<BillServiceDTO> services) {
            this.services = services;
        }
    }

    // ==========================================
    // INNER CLASS 2: Thông tin Dịch vụ trong Phòng
    // ==========================================
    public static class BillServiceDTO {
        private String serviceName;
        private Integer quantity;
        private Float price; // Giá lúc đặt
        private Double totalPrice; // price * quantity
        private String unit;

        // Getter & Setter
        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
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

        public Double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(Double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}