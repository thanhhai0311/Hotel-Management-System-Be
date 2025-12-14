package com.javaweb.config;

import com.javaweb.security.CustomAccessDeniedHandler;
import com.javaweb.security.CustomUserDetailsService;
import com.javaweb.security.JwtAuthenticationEntryPoint;
import com.javaweb.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//        	.cors().configurationSource(request -> new org.springframework.web.cors.CorsConfiguration().applyPermitDefaultValues())
                .cors()
                .and()
                // Tắt CSRF vì ta dùng JWT, không cần session
                .csrf().disable()

                // Xử lý lỗi xác thực và phân quyền
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .accessDeniedHandler(accessDeniedHandler)
                .and()

                // Không tạo session - dùng stateless JWT
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // Cấu hình quyền truy cập API
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // Public endpoints
                .antMatchers(
                        "/api/auth/**",          // login, register
                        "/api/account/active/**",// kích hoạt tài khoản
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**"
                ).permitAll()

                // Quyền theo vai trò
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/staff/**").hasAnyRole("STAFF", "ADMIN")
                .antMatchers("/api/customer/**").hasRole("CUSTOMER")

                // ========== AUTHENTICATION ==========
                .antMatchers("/api/auth/**").permitAll() // login, register public

                // ========== IMAGES ==========
                .antMatchers(HttpMethod.POST, "/api/images/upload").hasAnyRole("ADMIN", "CUSTOMER", "STAFF")

                // ========== USER PROFILE ==========
                .antMatchers(HttpMethod.PUT, "/api/user/profile").hasAnyRole("ADMIN", "CUSTOMER", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/user/me").hasAnyRole("ADMIN", "CUSTOMER", "STAFF")
                .antMatchers(HttpMethod.PUT, "/api/user/{id}/update").hasRole("ADMIN") // chỉ admin update user khác

                // ========== USER MANAGEMENT ==========
                .antMatchers(HttpMethod.GET, "/api/user/getAll").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.GET, "/api/user/{id}").hasRole("ADMIN")

                // ========== ACCOUNT ==========
                .antMatchers(HttpMethod.GET, "/api/account/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/account/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/account/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/account/**").hasRole("ADMIN")

                // ========== ROOM TYPES ==========
                .antMatchers(HttpMethod.POST, "/api/roomtypes/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/roomtypes/update/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/roomtypes/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/roomtypes/**").permitAll()

                // ========== SERVICES ==========
                .antMatchers(HttpMethod.POST, "/api/services/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/services/update/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/services/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/services/**").permitAll()

                // ========== SERVICE CATEGORY ==========
                .antMatchers(HttpMethod.POST, "/api/service-category/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/service-category/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/service-category/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/service-category/**").permitAll()

                // ========== ROOM STATUS ==========
                .antMatchers(HttpMethod.POST, "/api/room-statuses/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/room-statuses/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/room-statuses/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/room-statuses/**").permitAll()

                // ========== ROOM ==========
                .antMatchers(HttpMethod.GET, "/api/rooms/getAll", "/api/rooms/search").permitAll()
                .antMatchers(HttpMethod.POST, "/api/rooms/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/rooms/update/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/rooms/delete/**").hasRole("ADMIN")

                // ========== PROMOTION ==========
                .antMatchers(HttpMethod.POST, "/api/promotions/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/promotions/update/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/promotions/delete/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/promotions/update-status/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/promotions/getAll", "/api/promotions/search", "/api/promotions/{id}").permitAll()

                // ========== ROOM PROMOTION ==========
                .antMatchers(HttpMethod.POST, "/api/room-promotions/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/room-promotions/update/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/room-promotions/delete/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/room-promotions/**").permitAll()

                // ========== REVIEW ==========
                .antMatchers(HttpMethod.POST, "/api/reviews/create").hasAnyRole("CUSTOMER", "ADMIN") // tạo review
                .antMatchers(HttpMethod.PUT, "/api/reviews/update/**").hasAnyRole("CUSTOMER", "ADMIN") // cập nhật review
                .antMatchers(HttpMethod.DELETE, "/api/reviews/**").hasAnyRole("CUSTOMER", "ADMIN") // xóa review
                .antMatchers(HttpMethod.GET,
                        "/api/reviews/getAll",
                        "/api/reviews/search",
                        "/api/reviews/{id}",
                        "/api/reviews/**"
                ).permitAll() // ai cũng có thể xem review

                // ========== REVIEW IMAGE ==========
                .antMatchers(HttpMethod.DELETE, "/api/review-images/deleteBySrc").hasAnyRole("CUSTOMER", "ADMIN") // xóa ảnh theo src
                .antMatchers(HttpMethod.POST, "/api/review-images/**").hasAnyRole("CUSTOMER", "ADMIN") // upload ảnh (nếu sau này thêm)
                .antMatchers(HttpMethod.GET, "/api/review-images/**").permitAll() // ai cũng có thể xem ảnh review

                // ========= ROOM IMAGES =========
                .antMatchers(HttpMethod.DELETE, "/api/room-images/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/room-images/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/room-images/**").permitAll()

                // ========= SERVICE IMAGES =========
                .antMatchers(HttpMethod.POST, "/api/service-images/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/service-images/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/service-images/**").permitAll()

                // ========= ROOM TYPE IMAGES =========
                .antMatchers(HttpMethod.POST, "/api/room-type-images/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/room-type-images/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/room-type-images/**").permitAll()

                // ========== SHIFT ==========
                .antMatchers(HttpMethod.GET, "/api/shifts/**").hasAnyRole("ADMIN", "STAFF")
                .antMatchers(HttpMethod.POST, "/api/shifts/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/shifts/update/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/shifts/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/shifts/update-active/**").hasRole("ADMIN")

                // ========== SHIFTING ==========
                .antMatchers(HttpMethod.GET, "/api/shiftings/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/shiftings/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/shiftings/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/shiftings/**").hasRole("ADMIN")

                // ========== BOOKING ROOMS ==========
                .antMatchers(HttpMethod.POST, "/api/booking/create").hasAnyRole("CUSTOMER", "STAFF", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/bookings/{id}").hasAnyRole("CUSTOMER", "STAFF", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/bookings/getAll").hasAnyRole("CUSTOMER", "STAFF", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/bookings/cancel/**").hasAnyRole("CUSTOMER", "STAFF", "ADMIN")
                
                // ========== ROLES ==========
                .antMatchers("/api/roles/**").hasRole("ADMIN")


                // ========== TEST API ==========
                .antMatchers("/test/**").permitAll()

//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Các request còn lại cần xác thực
                .anyRequest().authenticated()
                .and()

                // Thêm filter kiểm tra JWT trước UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
