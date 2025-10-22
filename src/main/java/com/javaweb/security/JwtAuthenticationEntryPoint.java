package com.javaweb.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * JwtAuthenticationEntryPoint
 * ---------------------------
 * Class này được Spring Security gọi khi người dùng
 * truy cập vào API yêu cầu xác thực mà:
 *  - Không có token JWT
 *  - Hoặc token không hợp lệ / hết hạn
 * 
 * Nhiệm vụ: trả về mã lỗi 401 (Unauthorized)
 * kèm thông báo JSON rõ ràng.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, ServletException {

        // Trả mã lỗi 401 (chưa xác thực)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");

        // Thông báo lỗi rõ ràng
        PrintWriter writer = response.getWriter();
        writer.println("{");
        writer.println("  \"status\": 401,");
        writer.println("  \"error\": \"Unauthorized\",");
        writer.println("  \"message\": \"Bạn chưa đăng nhập hoặc token không hợp lệ\",");
        writer.println("  \"path\": \"" + request.getRequestURI() + "\"");
        writer.println("}");
        writer.flush();
    }
}