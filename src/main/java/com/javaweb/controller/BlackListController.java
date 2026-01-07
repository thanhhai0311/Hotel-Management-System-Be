package com.javaweb.controller;

import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/blacklist")
public class BlackListController {
    @Autowired
    private BlacklistService blacklistService;

    @PostMapping("/reset/{customerId}")
    public ResponseEntity<String> resetCustomerLimit(@PathVariable Integer customerId) {
        try {
            blacklistService.resetBlackList(customerId);
            return ResponseEntity.ok("Đã gỡ bỏ giới hạn đặt phòng cho khách hàng thành công!");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/checkUser/{customerId}")
    public ResponseEntity<?> checkUserInBlackList(@PathVariable Integer customerId) {
        try {
            boolean res = blacklistService.isBlackList(customerId);
            ApiResponse<Boolean> response = new ApiResponse<>(true, HttpStatus.OK.value(),
                    "Cập nhật đánh giá thành công", res, "api/blacklist/checkUser/{customerId}/" + customerId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
