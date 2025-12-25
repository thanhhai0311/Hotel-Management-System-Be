package com.javaweb.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class VietQRUtils {
    // CẤU HÌNH TÀI KHOẢN NGÂN HÀNG NHẬN TIỀN
    private static final String BANK_ID = "MB"; // Ngân hàng (MB, VCB, TPB...)
    private static final String ACCOUNT_NO = "0967726885"; // Số tài khoản của bạn
    private static final String TEMPLATE = "compact"; // Giao diện QR

    public static String generateQRUrl(double amount, String content) {
        try {
            // Encode nội dung tiếng Việt/ký tự đặc biệt
            String encodedContent = URLEncoder.encode(content, StandardCharsets.UTF_8.toString());
            // VietQR yêu cầu số nguyên (long) cho amount
            long amountLong = (long) amount;

            return String.format("https://img.vietqr.io/image/%s-%s-%s.png?amount=%d&addInfo=%s",
                    BANK_ID, ACCOUNT_NO, TEMPLATE, amountLong, encodedContent);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}