package com.javaweb.controller;

import com.javaweb.model.dto.BillDTO.BillResponseDTO;
import com.javaweb.model.dto.CheckoutDTO.CheckoutInfoDTO;
import com.javaweb.model.entity.BillEntity;
import com.javaweb.repository.BillRepository;
import com.javaweb.service.BillService;
import com.javaweb.service.BookingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BookingRoomService bookingRoomService;

    @GetMapping
    public ResponseEntity<List<BillResponseDTO>> getAllBills(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit
    ) {
        List<BillResponseDTO> result = billService.getAllBills(page, limit);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDTO> getBillById(@PathVariable Integer id) {
        BillResponseDTO result = billService.getBillById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("{id}/status")
    public ResponseEntity<Map<String, Object>> getBillStatus(@PathVariable Integer id) {
        BillEntity bill = billRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Map<String, Object> res = new HashMap<>();
        // ID 2 = PAID
        boolean isPaid = bill.getPaymentStatus() != null && bill.getPaymentStatus().getId() == 2;

        res.put("status", isPaid ? "PAID" : "UNPAID");
        return ResponseEntity.ok(res);
    }

    @PostMapping("{billId}/checkout-confirm")
    public ResponseEntity<String> confirmCheckoutByBill(
            @PathVariable Integer billId,
            @RequestParam Integer paymentMethodId
    ) {
        bookingRoomService.confirmCheckoutByBill(billId, paymentMethodId);
        return ResponseEntity.ok("Thanh toán và Checkout toàn bộ hóa đơn thành công!");
    }

    @GetMapping("/{id}/payment-info")
    public ResponseEntity<CheckoutInfoDTO> getPaymentInfoByBill(@PathVariable("id") Integer billId) {
        // Gọi Service xử lý logic lấy thông tin và tạo QR
        CheckoutInfoDTO info = bookingRoomService.getPaymentInfoByBill(billId);
        return ResponseEntity.ok(info);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<BillResponseDTO> updateBillStatus(@PathVariable("id") Integer id) {
        BillResponseDTO billRes = billService.updateStatusBill(id);
        return ResponseEntity.ok(billRes);
    }
}