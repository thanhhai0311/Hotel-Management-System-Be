package com.javaweb.service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.javaweb.model.dto.ReviewDTO.ReviewCreateDTO;
import com.javaweb.model.dto.ReviewDTO.ReviewResponseDTO;
import com.javaweb.model.dto.ReviewDTO.ReviewUpdateDTO;

public interface ReviewService {
	ReviewResponseDTO createReview(ReviewCreateDTO dto) throws IOException;
	ReviewResponseDTO updateReview(Integer id, ReviewUpdateDTO dto) throws IOException;
	Map<String, Object> getAllReviews(Integer page, Integer size);
	void deleteReview(Integer id);
	ReviewResponseDTO getReviewById(Integer id);
	Page<ReviewResponseDTO> searchReviews(
	        String details,
	        Integer star,
	        String type,
	        Integer customerId,
	        Date day,
	        int page,
	        int size
	    );
}
