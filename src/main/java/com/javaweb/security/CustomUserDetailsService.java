package com.javaweb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.javaweb.model.entity.CustomerEntity;
import com.javaweb.model.entity.EmployeeEntity;
import com.javaweb.repository.CustomerRepository;
import com.javaweb.repository.EmployeeRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomerEntity customer = customerRepository.findByEmail(username).orElse(null);
        if (customer != null) {
            return User.builder()
                    .username(customer.getEmail())
                    .password(customer.getPassword())
                    .roles("CUSTOMER")
                    .build();
        }

        EmployeeEntity employee = employeeRepository.findByEmail(username).orElse(null);
        if (employee != null) {
            return User.builder()
                    .username(employee.getEmail())
                    .password(employee.getPassword())
                    .roles("EMPLOYEE")
                    .build();
        }

        throw new UsernameNotFoundException("User not found: " + username);
    }
}
