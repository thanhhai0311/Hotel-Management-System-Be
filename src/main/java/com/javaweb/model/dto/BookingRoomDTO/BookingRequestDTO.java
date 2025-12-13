package com.javaweb.model.dto.BookingRoomDTO;

import java.util.List;

public class BookingRequestDTO {
    private Integer customerId;
    private List<BookingItemDTO> bookings;
    private String customerName;
    private String customerPhone;
    //    private String customerEmail;
    private Integer paymentMethodId;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public List<BookingItemDTO> getBookings() {
        return bookings;
    }

    public void setBookings(List<BookingItemDTO> bookings) {
        this.bookings = bookings;
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

//    public String getCustomerEmail() {
//        return customerEmail;
//    }
//
//    public void setCustomerEmail(String customerEmail) {
//        this.customerEmail = customerEmail;
//    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
}
