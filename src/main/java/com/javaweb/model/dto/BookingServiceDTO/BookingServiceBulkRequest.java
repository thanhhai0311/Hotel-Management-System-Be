package com.javaweb.model.dto.BookingServiceDTO;

import java.util.List;

public class BookingServiceBulkRequest {
    private Integer bookingRoomId;
    private List<ServiceItem> items; //

    // Class con để hứng từng món nhỏ
    public static class ServiceItem {
        private Integer serviceId;
        private Integer quantity;

        public Integer getServiceId() {
            return serviceId;
        }

        public void setServiceId(Integer serviceId) {
            this.serviceId = serviceId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }

    public Integer getBookingRoomId() {
        return bookingRoomId;
    }

    public void setBookingRoomId(Integer bookingRoomId) {
        this.bookingRoomId = bookingRoomId;
    }

    public List<ServiceItem> getItems() {
        return items;
    }

    public void setItems(List<ServiceItem> items) {
        this.items = items;
    }
}