package com.javaweb.api;

import com.javaweb.model.dto.WebhookDTO.SePayWebhookDTO;
import com.javaweb.model.entity.BillEntity;
import com.javaweb.repository.BillRepository;
import com.javaweb.service.BookingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/sepay")
public class WebhookAPI {

    @Value("${sepay.api-token}")
    private String sepayApiToken;

    @Autowired
    private BookingRoomService bookingRoomService;

    @Autowired
    private BillRepository billRepository;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleSePayWebhook(
            @RequestHeader(value = "Authorization", defaultValue = "") String authorization,
            @RequestBody SePayWebhookDTO payload
    ) {

        System.out.println("1. Token Server đang giữ (Config): " + sepayApiToken);
        System.out.println("2. Token Swagger gửi lên (Header): " + authorization);
        
        // 1. Bảo mật: Check Token SePay
        if (!authorization.contains(sepayApiToken)) {
            return ResponseEntity.status(403).body("Unauthorized");
        }

        // 2. Chỉ xử lý tiền vào
        if (payload.getTransferAmount() == null || payload.getTransferAmount() <= 0) {
            return ResponseEntity.ok("Ignored");
        }

        // 3. Phân tích nội dung để lấy Bill ID (VD: "THANHTOAN BILL 102")
        String content = payload.getContent();
        Integer billId = extractBillId(content);

        if (billId != null) {
            processPayment(billId, payload.getTransferAmount());
        }

        return ResponseEntity.ok("Success");
    }

    private Integer extractBillId(String content) {
        if (content == null) return null;
        // Regex bắt chữ "BILL" sau đó là số
        Pattern pattern = Pattern.compile("BILL\\s*(\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            try {
                return Integer.parseInt(matcher.group(1));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private void processPayment(Integer billId, Float receivedAmount) {
        BillEntity bill = billRepository.findById(billId).orElse(null);
        if (bill == null) return;

        // Bỏ qua nếu đã thanh toán
        if (bill.getPaymentStatus() != null && bill.getPaymentStatus().getId() == 2) {
            return;
        }

        // So sánh tiền
        if (receivedAmount >= (bill.getTotalAfterTax() - 1000)) {
            // paymentMethodId = 2 (Chuyển khoản)
            bookingRoomService.confirmCheckoutByBill(billId, 2);

            System.out.println("SePay: Đã Checkout thành công cho Bill " + billId);
        }
    }
}