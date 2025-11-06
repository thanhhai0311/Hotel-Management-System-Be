package com.javaweb.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.converter.ReviewConverter;
import com.javaweb.model.dto.ReviewDTO.ReviewCreateDTO;
import com.javaweb.model.dto.ReviewDTO.ReviewResponseDTO;
import com.javaweb.model.dto.ReviewDTO.ReviewUpdateDTO;
import com.javaweb.model.entity.ReviewEntity;
import com.javaweb.model.entity.ReviewImageEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.repository.ReviewImageRepository;
import com.javaweb.repository.ReviewRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.CloudinaryService;
import com.javaweb.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewImageRepository reviewImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ReviewConverter reviewConverter;

    @Override
    public ReviewResponseDTO createReview(ReviewCreateDTO dto) throws IOException {
        ReviewEntity review = new ReviewEntity();
        review.setDetails(dto.getDetails());
        review.setStar(dto.getStar());
        review.setType(dto.getType());
        review.setDay(new Date());

        // Tìm khách hàng
        UserEntity customer = userRepository.findById(dto.getIdCustomer())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khách hàng"));
        review.setCustomer(customer);

        // Upload ảnh nếu có
        List<ReviewImageEntity> images = new ArrayList<>();
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            for (MultipartFile file : dto.getImages()) {
                if (file != null && !file.isEmpty()) {
                    String url = cloudinaryService.uploadFile(file);
                    ReviewImageEntity img = new ReviewImageEntity();
                    img.setSrc(url);
                    img.setReview(review);
                    img.setDetails("Ảnh cho review của KH " + img.getReview().getCustomer().getName());
                    images.add(img);
                }
            }
        }

        review.setReviewImages(images);
        ReviewEntity saved = reviewRepository.save(review);
        return reviewConverter.toResponseDTO(saved);
    }
    
    @Override
    @Transactional
    public ReviewResponseDTO updateReview(Integer id, ReviewUpdateDTO dto) throws IOException {
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đánh giá có id = " + id));

        // === Cập nhật thông tin cơ bản ===
        if (dto.getDetails() != null && !dto.getDetails().trim().isEmpty())
            review.setDetails(dto.getDetails());
        if (dto.getStar() != null)
            review.setStar(dto.getStar());
        if (dto.getType() != null)
            review.setType(dto.getType());
        review.setDay(new Date());

        if (dto.getIdCustomer() != null) {
            UserEntity customer = userRepository.findById(dto.getIdCustomer())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Không tìm thấy khách hàng id = " + dto.getIdCustomer()));
            review.setCustomer(customer);
        }

        // === Xóa ảnh cũ ===
        if (dto.getImageIdsToDelete() != null && !dto.getImageIdsToDelete().isEmpty()) {
            for (Integer imgId : dto.getImageIdsToDelete()) {
                ReviewImageEntity image = reviewImageRepository.findById(imgId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Không tìm thấy ảnh có id = " + imgId));
                if (!image.getReview().getId().equals(review.getId())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Ảnh id = " + imgId + " không thuộc về đánh giá id = " + review.getId());
                }
                try {
                    cloudinaryService.deleteFileByUrl(image.getSrc());
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Lỗi khi xóa ảnh Cloudinary: " + e.getMessage());
                }
                reviewImageRepository.delete(image);
                review.getReviewImages().remove(image);
            }
        }

        // === Thêm ảnh mới ===
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            for (MultipartFile file : dto.getImages()) {
                if (file != null && !file.isEmpty()) {
                    String url = cloudinaryService.uploadFile(file);
                    ReviewImageEntity newImg = new ReviewImageEntity();
                    newImg.setSrc(url);
                    newImg.setReview(review);
                    reviewImageRepository.save(newImg);
                    review.getReviewImages().add(newImg);
                }
            }
        }

        ReviewEntity updated = reviewRepository.save(review);
        return reviewConverter.toResponseDTO(updated);
    }

    @Override
    public Map<String, Object> getAllReviews(Integer page, Integer size) {
        Map<String, Object> response = new HashMap<>();

        // Nếu không truyền page và size -> lấy toàn bộ
        if (page == null || size == null) {
            List<ReviewEntity> allReviews = reviewRepository.findAll(Sort.by(Sort.Direction.DESC, "day"));
            List<ReviewResponseDTO> reviewDTOs = allReviews.stream()
                    .map(reviewConverter::toResponseDTO)
                    .collect(Collectors.toList());

            response.put("reviews", reviewDTOs);
            response.put("totalItems", reviewDTOs.size());
            response.put("totalPages", 1);
            response.put("currentPage", 0);
            return response;
        }

        // Nếu có phân trang
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "day"));
        Page<ReviewEntity> reviewPage = reviewRepository.findAll(pageable);

        List<ReviewResponseDTO> reviewDTOs = reviewPage.getContent().stream()
                .map(reviewConverter::toResponseDTO)
                .collect(Collectors.toList());

        response.put("reviews", reviewDTOs);
        response.put("currentPage", reviewPage.getNumber());
        response.put("totalItems", reviewPage.getTotalElements());
        response.put("totalPages", reviewPage.getTotalPages());

        return response;
    }

    @Transactional
	@Override
	public void deleteReview(Integer id) {
    	// Tìm review theo ID
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Không tìm thấy đánh giá có ID = " + id));

        // Xóa ảnh liên quan (nếu có)
        if (review.getReviewImages() != null && !review.getReviewImages().isEmpty()) {
            for (ReviewImageEntity image : review.getReviewImages()) {
                try {
                    cloudinaryService.deleteFileByUrl(image.getSrc());
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Lỗi khi xóa ảnh Cloudinary: " + e.getMessage());
                }
                reviewImageRepository.delete(image);
            }
        }

        // Xóa review
        reviewRepository.delete(review);
    }

    @Override
    public ReviewResponseDTO getReviewById(Integer id) {
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy đánh giá có ID = " + id));

        return reviewConverter.toResponseDTO(review);
    }

    @Override
    public Page<ReviewResponseDTO> searchReviews(
            String details,
            Integer star,
            String type,
            Integer customerId,
            Date day,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "day"));

        Specification<ReviewEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (details != null && !details.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("details")), "%" + details.toLowerCase() + "%"));
            }

            if (star != null) {
                predicates.add(cb.equal(root.get("star"), star));
            }

            if (type != null && !type.trim().isEmpty()) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            if (customerId != null) {
                predicates.add(cb.equal(root.get("customer").get("id"), customerId));
            }

            if (day != null) {
                // So sánh theo ngày, có thể dùng between nếu muốn mở rộng theo khoảng
                predicates.add(cb.equal(root.get("day"), day));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<ReviewEntity> reviewPage = reviewRepository.findAll(spec, pageable);

        return reviewPage.map(reviewConverter::toResponseDTO);
    }
    
}
