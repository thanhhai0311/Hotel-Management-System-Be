package com.javaweb.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.javaweb.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String path = request.getServletPath();

		// ✅ 1. Bỏ qua filter cho login & register
		if (path.startsWith("/api/auth")) {
			chain.doFilter(request, response);
			return;
		}

		final String header = request.getHeader("Authorization");
		String email = null;
		String token = null;

		if (header != null && header.startsWith("Bearer ")) {
			token = header.substring(7);
			try {
				email = jwtUtil.extractEmail(token);
			} catch (Exception e) {
				logger.error("Lỗi khi đọc token: " + e.getMessage());
			}
		}

		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(email);
			if (jwtUtil.validateToken(token)) {
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
						userDetails.getAuthorities());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
				System.out.println("[FILTER] Xác thực thành công cho user: " + email);
			} else {
				System.out.println("[FILTER] Token không hợp lệ hoặc đã hết hạn.");
			}
		}

		chain.doFilter(request, response);
	}
}
