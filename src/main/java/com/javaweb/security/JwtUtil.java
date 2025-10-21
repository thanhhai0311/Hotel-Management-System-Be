package com.javaweb.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    private Key signingKey;

    // ✅ Chỉ khởi tạo key 1 lần khi app start
    @PostConstruct
    public void initKey() {
        signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        System.out.println("🚀 JwtUtil initialized!");
        System.out.println("🔑 SECRET_KEY = " + SECRET_KEY);
        System.out.println("✅ signingKey object = " + signingKey);
    }

    public String generateToken(String email, String role) {
        System.out.println("🟢 [JWT-GEN] Dùng key: " + signingKey);
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(signingKey, SignatureAlgorithm.HS256) // ✅ chỉ dùng key đã khởi tạo
                .compact();
    }

    public String extractEmail(String token) {
        System.out.println("🔵 [JWT-VERIFY] Dùng key: " + signingKey);
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            System.out.println("❌ [JWT] Token không hợp lệ: " + e.getMessage());
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token đã hết hạn!");
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Token không hợp lệ!");
        } catch (SignatureException e) {
            throw new RuntimeException("Chữ ký token không hợp lệ!");
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xử lý JWT: " + e.getMessage());
        }
    }
}
