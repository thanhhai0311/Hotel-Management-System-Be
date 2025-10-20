package com.javaweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.javaweb.model.dto.LoginRequest;
import com.javaweb.model.dto.LoginResponse;
import com.javaweb.model.entity.CustomerEntity;
import com.javaweb.model.entity.EmployeeEntity;
import com.javaweb.repository.CustomerRepository;
import com.javaweb.repository.EmployeeRepository;
import com.javaweb.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	public LoginResponse loginCustomer(LoginRequest request) {
//        CustomerEntity customer = customerRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("Customer không tồn tại"));
//
//        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
//            throw new RuntimeException("Sai mật khẩu");
//        }
//
//        String token = jwtUtil.generateToken(customer.getEmail(), "CUSTOMER");
//
//        return new LoginResponse(token, "CUSTOMER", customer.getName(), customer.getEmail());
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		CustomerEntity customer = customerRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("Customer không tồn tại"));

		String token = jwtUtil.generateToken(customer.getEmail(), "ROLE_CUSTOMER", "");

		return new LoginResponse(token, "CUSTOMER", customer.getName(), customer.getEmail());

	}

	public LoginResponse loginEmployee(LoginRequest request) {
//        EmployeeEntity employee = employeeRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("Employee không tồn tại"));
//
//        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
//            throw new RuntimeException("Sai mật khẩu");
//        }
//
//        String token = jwtUtil.generateToken(employee.getEmail(), "EMPLOYEE");
//
//        return new LoginResponse(token, "EMPLOYEE", employee.getName(), employee.getEmail());
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		EmployeeEntity employee = employeeRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new RuntimeException("Employee không tồn tại"));

		String token = jwtUtil.generateToken(employee.getEmail(), "ROLE_EMPLOYEE", employee.getRole().getName());

		return new LoginResponse(token, "EMPLOYEE", employee.getName(), employee.getEmail());
	}
}