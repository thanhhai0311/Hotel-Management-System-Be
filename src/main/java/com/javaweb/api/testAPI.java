package com.javaweb.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.utils.ConnectionJDBCUtils;

@RestController
public class testAPI {
	@Autowired
	private ConnectionJDBCUtils conn;
	
	@GetMapping(value="/test")
	public String test () {
		conn.getConnection();
		return "success";
	}
}