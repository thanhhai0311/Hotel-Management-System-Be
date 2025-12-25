package com.javaweb.model.dto.CheckoutDTO;

import lombok.Data;

import java.util.List;

@Data
public class CheckoutInfoDTO {
    private Integer billId;
    private String customerName;
    private List<Integer> roomNumbers;
    private Float totalAmount;
    private String qrUrl;       // Link ảnh QR
    private String paymentContent; // Nội dung CK (VD: BILL 123)
}