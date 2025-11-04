package com.javaweb.config;

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

import com.javaweb.security.CustomAccessDeniedHandler;
import com.javaweb.security.CustomUserDetailsService;
import com.javaweb.security.JwtAuthenticationEntryPoint;
import com.javaweb.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

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
        	.cors().configurationSource(request -> new org.springframework.web.cors.CorsConfiguration().applyPermitDefaultValues())
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

                // ========== ACCOUNT ==========
                .antMatchers(HttpMethod.GET, "/api/account/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/account/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/account/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/account/**").hasRole("ADMIN")

                // ========== ROOM TYPES ==========
                .antMatchers(HttpMethod.GET, "/api/roomtypes/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/roomtypes/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/roomtypes/update/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/roomtypes/**").hasRole("ADMIN")

                // ========== SERVICES ==========
                .antMatchers(HttpMethod.GET, "/api/services/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/services/create").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/services/update/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/services/**").hasRole("ADMIN")

                // ========== SERVICE CATEGORY ==========
                .antMatchers(HttpMethod.GET, "/api/service-category/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/service-category/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/service-category/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/service-category/**").hasRole("ADMIN")

                // ========== ROOM STATUS ==========
                .antMatchers(HttpMethod.GET, "/api/room-statuses/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/room-statuses/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/room-statuses/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/room-statuses/**").hasRole("ADMIN")
                
                // ========== ROOM ==========
                .antMatchers("/api/rooms/getAll", "/api/rooms/search", "/api/rooms/**").permitAll()
                .antMatchers("/api/rooms/create", "/api/rooms/update/**", "/api/rooms/delete/**", "/api/rooms/{id}").hasRole("ADMIN")

			    // ========== PROMOTION ==========
                .antMatchers(HttpMethod.GET, "/api/promotions/getAll", "/api/promotions/search", "/api/promotions/{id}").permitAll()
			    .antMatchers(HttpMethod.POST, "/api/promotions/create").hasRole("ADMIN")
			    .antMatchers(HttpMethod.PUT, "/api/promotions/update/**").hasRole("ADMIN")
			    .antMatchers(HttpMethod.DELETE, "/api/promotions/delete/**").hasRole("ADMIN")
			    .antMatchers(HttpMethod.PUT, "/api/promotions/update-status/**").hasRole("ADMIN")
			    
			    // ========== ROOM PROMOTION ==========
			    .antMatchers(HttpMethod.GET, "/api/room-promotions/**").permitAll()
			    .antMatchers(HttpMethod.POST, "/api/room-promotions/create").hasRole("ADMIN")
			    .antMatchers(HttpMethod.PUT, "/api/room-promotions/update/**").hasRole("ADMIN")
			    .antMatchers(HttpMethod.DELETE, "/api/room-promotions/delete/**").hasRole("ADMIN")
			    
			    // ========== REVIEW ==========
			    .antMatchers(HttpMethod.GET, "/api/reviews/**").permitAll() 
			    .antMatchers(HttpMethod.POST, "/api/reviews").hasAnyRole("CUSTOMER", "ADMIN")
			    .antMatchers(HttpMethod.PUT, "/api/reviews/**").hasAnyRole("CUSTOMER", "ADMIN")
			    .antMatchers(HttpMethod.DELETE, "/api/reviews/**").hasAnyRole("CUSTOMER","ADMIN")

                
                // ========== TEST API ==========
                .antMatchers("/test/**").permitAll()

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
