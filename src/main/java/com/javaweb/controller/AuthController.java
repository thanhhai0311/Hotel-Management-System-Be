package com.javaweb.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.javaweb.model.dto.CustomerRegisterRequest;
import com.javaweb.model.dto.EmployeeRegisterRequest;
import com.javaweb.model.entity.CustomerEntity;
import com.javaweb.model.entity.EmployeeEntity;
import com.javaweb.model.entity.RoleEntity;
import com.javaweb.repository.CustomerRepository;
import com.javaweb.repository.EmployeeRepository;
import com.javaweb.repository.RoleRepository;
import com.javaweb.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
//    @PostMapping("/register/customer")
//    public ResponseEntity<?> registerCustomer(@RequestBody CustomerEntity customer) {
//        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
//            return ResponseEntity.badRequest().body("Email đã tồn tại!");
//        }
//        
//        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
//        System.out.println(customer);
//        customerRepository.save(customer);
//
//        return ResponseEntity.ok("Đăng ký khách hàng thành công!");
//    }
    
    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerRegisterRequest request) {
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        }

        CustomerEntity customer = new CustomerEntity();
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setGender(request.getGender());
        customer.setAddress(request.getAddress());
        customer.setDob(request.getDob());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        customerRepository.save(customer);

        return ResponseEntity.ok("Đăng ký khách hàng thành công!");
    }
    
    @PostMapping("/register/employee")
    public ResponseEntity<?> registerEmployee(@RequestBody EmployeeRegisterRequest request) {
        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        }
        
        if (request.getIdRole() == null) {
            return ResponseEntity.badRequest().body("Vui lòng chọn role cho nhân viên!");
        }

        // ✅ Kiểm tra role có tồn tại không
        RoleEntity role = roleRepository.findById(request.getIdRole())
                .orElse(null);
        if (role == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Role không tồn tại!");
        }

        EmployeeEntity employee = new EmployeeEntity();
        employee.setName(request.getName());
        employee.setPhone(request.getPhone());
        employee.setGender(request.getGender());
        employee.setAddress(request.getAddress());
        employee.setDob(request.getDob());
        employee.setEmail(request.getEmail());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setRole(role);

        employeeRepository.save(employee);

        return ResponseEntity.ok("Đăng ký nhân viên thành công với role: " + role.getName());
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            String token = jwtUtil.generateToken(authRequest.getEmail(), role);
            return ResponseEntity.ok(new JwtResponse(token, role));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai email hoặc mật khẩu");
        }
    }
    
//    @GetMapping("/me")
//    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập!");
//        }
//
//        String email = authentication.getName();
//        Map<String, Object> response = new HashMap<>();
//        response.put("email", email);
//
//        // Tìm role từ DB (tùy vào bảng)
//        if (customerRepository.findByEmail(email).isPresent()) {
//            response.put("role", "CUSTOMER");
//        } else {
//            employeeRepository.findByEmail(email)
//                    .ifPresent(e -> response.put("role", e.getRole().getName()));
//        }
//
//        return ResponseEntity.ok(response);
//    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
    	System.out.println("JWT " + authentication);
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Chưa đăng nhập!");
        }

        String email = authentication.getName();

        // Tìm user trong Customer hoặc Employee
        Optional<CustomerEntity> customerOpt = customerRepository.findByEmail(email);
        Optional<EmployeeEntity> employeeOpt = employeeRepository.findByEmail(email);

        if (customerOpt.isPresent()) {
            CustomerEntity c = customerOpt.get();

            // Ẩn password khi trả về
            Map<String, Object> result = new HashMap<>();
            result.put("id", c.getId());
            result.put("name", c.getName());
            result.put("phone", c.getPhone());
            result.put("gender", c.getGender());
            result.put("address", c.getAddress());
            result.put("dob", c.getDob());
            result.put("email", c.getEmail());
            result.put("role", "CUSTOMER");

            return ResponseEntity.ok(result);

        } else if (employeeOpt.isPresent()) {
            EmployeeEntity e = employeeOpt.get();

            Map<String, Object> result = new HashMap<>();
            result.put("id", e.getId());
            result.put("name", e.getName());
            result.put("phone", e.getPhone());
            result.put("gender", e.getGender());
            result.put("address", e.getAddress());
            result.put("dob", e.getDob());
            result.put("email", e.getEmail());
            result.put("role", e.getRole() != null ? e.getRole().getName() : "UNKNOWN");

            return ResponseEntity.ok(result);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng!");
    }

    static class AuthRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String username) { this.email = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    static class JwtResponse {
        private String token;
        private String role;

        public JwtResponse(String token, String role) {
            this.token = token;
            this.role = role;
        }

        public String getToken() { return token; }
        public String getRole() { return role; }
    }
}
