package com.javaweb.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.javaweb.model.dto.ReviewDTO.ReviewDTO;

public interface ReviewService {
    void deleteReview(Integer id);
    ReviewDTO getReviewById(Integer id);
    Map<String, Object> getAllReviews(Integer page, Integer pageSize);
    ReviewDTO createReview(ReviewDTO dto, MultipartFile[] images);
    ReviewDTO updateReview(Integer id, ReviewDTO dto, MultipartFile[] newImages, List<Integer> deleteImageIds);
}
