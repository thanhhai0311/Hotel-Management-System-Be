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
                registry.addMapping("/api/**")
                        // Cho phép tất cả môi trường bạn đang dùng
                        .allowedOriginPatterns(
                            "http://localhost:5173",                    // React local
                            "https://hotel-management-fe.vercel.app",   // FE deploy
                            "https://hotel-management-system-be-1.onrender.com", // Render self-call
                            "*" // Cho phép mobile Flutter (vì Flutter không gửi Origin)
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
