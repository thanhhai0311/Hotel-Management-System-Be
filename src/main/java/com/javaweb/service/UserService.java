package com.javaweb.service;

import com.javaweb.model.entity.UserEntity;

public interface UserService {
	UserEntity getUserFromToken(String token);
	UserEntity getCurrentUser();
}
