package com.javaweb.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
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
    
}
