package com.javaweb.security;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        // Cho phép các endpoint /auth/** đi qua không cần JWT
        if (path.equals("/auth/login") || path.equals("/auth/register/customer") || path.equals("/auth/register/employee")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        // Kiểm tra có header Authorization hay không
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                System.out.println("⚠️ Token không hợp lệ hoặc hết hạn: " + e.getMessage());
            }
        }

        // Nếu có username và chưa có Authentication trong context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 🔥 Quan trọng: gắn chi tiết request để Security hiểu request này đã xác thực
                authToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));

                // 🔥 Quan trọng: đưa Authentication vào context
                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println("✅ JWT xác thực thành công cho user: " + username);
            } else {
                System.out.println("❌ JWT không hợp lệ hoặc không khớp user!");
            }
        }

        filterChain.doFilter(request, response);
    }
}
