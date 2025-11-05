package com.javaweb.service;

import java.io.IOException;

import com.javaweb.model.dto.ReviewDTO.ReviewCreateDTO;
import com.javaweb.model.dto.ReviewDTO.ReviewResponseDTO;
import com.javaweb.model.dto.ReviewDTO.ReviewUpdateDTO;

public interface ReviewService {
	ReviewResponseDTO createReview(ReviewCreateDTO dto) throws IOException;
	ReviewResponseDTO updateReview(Integer id, ReviewUpdateDTO dto) throws IOException;
}
