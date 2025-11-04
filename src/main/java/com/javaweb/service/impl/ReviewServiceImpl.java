package com.javaweb.service.impl;

import com.javaweb.converter.ReviewConverter;
import com.javaweb.model.dto.ReviewDTO.ReviewDTO;
import com.javaweb.model.dto.ReviewImageDTO.ReviewImageDTO;
import com.javaweb.model.entity.ReviewEntity;
import com.javaweb.model.entity.ReviewImageEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.repository.ReviewImageRepository;
import com.javaweb.repository.ReviewRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewImageRepository reviewImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewConverter converter;

//    @Override
//    public ReviewDTO createReview(ReviewDTO dto) {
//        ReviewEntity review = converter.toEntity(dto);
//        review.setDay(new Date());
//
//        UserEntity customer = userRepository.findById(dto.getCustomerId())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khách hàng"));
//        review.setCustomer(customer);
//
//        ReviewEntity saved = reviewRepository.save(review);
//
//        if (dto.getReviewImages() != null) {
//            List<ReviewImageEntity> images = dto.getReviewImages().stream()
//                    .map(converter::toImageEntity)
//                    .peek(img -> img.setReview(saved))
//                    .collect(Collectors.toList());
//            reviewImageRepository.saveAll(images);
//            saved.setReviewImages(images);
//        }
//
//        return converter.toDTO(saved);
//    }
//
//    @Override
//    public ReviewDTO updateReview(Integer id, ReviewDTO dto) {
//        ReviewEntity review = reviewRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đánh giá"));
//
//        if (dto.getDetails() != null) review.setDetails(dto.getDetails());
//        if (dto.getStar() != null) review.setStar(dto.getStar());
//        if (dto.getType() != null) review.setType(dto.getType());
//
//        ReviewEntity updated = reviewRepository.save(review);
//        return converter.toDTO(updated);
//    }

    @Override
    public void deleteReview(Integer id) {
        if (!reviewRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy review để xóa");

        reviewRepository.deleteById(id);
    }

    @Override
    public ReviewDTO getReviewById(Integer id) {
        return converter.toDTO(
                reviewRepository.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy review"))
        );
    }

    @Override
    public Map<String, Object> getAllReviews(Integer page, Integer pageSize) {
        List<ReviewEntity> reviews;
        long totalElements;
        int totalPages;

        // Nếu không truyền page và pageSize → lấy tất cả
        if (page == null || pageSize == null) {
            reviews = reviewRepository.findAll();
            totalElements = reviews.size();
            totalPages = 1;
        } else {
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
            Page<ReviewEntity> pageResult = reviewRepository.findAll(pageable);
            reviews = pageResult.getContent();
            totalElements = pageResult.getTotalElements();
            totalPages = pageResult.getTotalPages();
        }

        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(converter::toDTO)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("content", reviewDTOs);
        result.put("totalElements", totalElements);
        result.put("totalPages", totalPages);
        result.put("page", page != null ? page : 0);
        result.put("pageSize", pageSize != null ? pageSize : totalElements);

        return result;
    }

	@Override
	public ReviewDTO createReview(ReviewDTO dto, MultipartFile[] images) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReviewDTO updateReview(Integer id, ReviewDTO dto, MultipartFile[] newImages, List<Integer> deleteImageIds) {
		// TODO Auto-generated method stub
		return null;
	}

}