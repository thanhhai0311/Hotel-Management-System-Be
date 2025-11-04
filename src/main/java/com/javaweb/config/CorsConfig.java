package com.javaweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        // Đừng dùng "*" khi allowCredentials = true
                        // Hãy liệt kê domain chính xác bạn dùng:
                        .allowedOrigins(
                            "http://localhost:5173",
                            "https://hotel-management-fe.vercel.app",
                            "capacitor://localhost",     // Flutter trên iOS (Capacitor)
                            "http://localhost",           // Flutter debug trên Android
                            "https://hotel-management-system-be-1.onrender.com"
                        )
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization", "Content-Type")
                        .allowCredentials(true);
            }
        };
    }
}
