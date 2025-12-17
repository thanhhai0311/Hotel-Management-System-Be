package com.javaweb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailServiceImpl {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otpCode) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("nguyenthanhhai03112003@gmail.com", "Hệ Thống Đặt Phòng");

        helper.setTo(toEmail);
        helper.setSubject("Mã xác thực OTP");

        String htmlContent = "<div style='font-family: Arial, sans-serif; padding: 20px; border: 1px solid #ddd;'>"
                + "<h2 style='color: #2C3E50;'>Xác thực tài khoản</h2>"
                + "<p>Xin chào,</p>"
                + "<p>Mã OTP của bạn là:</p>"
                + "<h1 style='color: #E74C3C; letter-spacing: 5px;'>" + otpCode + "</h1>"
                + "<p>Mã này có hiệu lực trong 5 phút.</p>"
                + "</div>";

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}