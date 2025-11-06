package com.javaweb.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.model.entity.ReviewImageEntity;
import com.javaweb.repository.ReviewImageRepository;
import com.javaweb.service.CloudinaryService;
import com.javaweb.service.ReviewImageService;

@Service
public class ReviewImageServiceImpl implements ReviewImageService {

	@Autowired
    private ReviewImageRepository reviewImageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;
    
    @Transactional
    @Override
    public void deleteImageBySrc(String src) {
        // Tìm ảnh theo src
        ReviewImageEntity image = reviewImageRepository.findBySrc(src)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy ảnh có đường dẫn: " + src));

        // Lấy người dùng hiện tại từ SecurityContext
        org.springframework.security.core.Authentication authentication =
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();

        // Nếu người dùng chưa đăng nhập
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn cần đăng nhập để xóa ảnh.");
        }

        String currentUsername = authentication.getName();
        System.out.println(currentUsername);

        // Kiểm tra vai trò
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            // Nếu là customer thì chỉ được xóa ảnh thuộc review của chính họ
            String ownerUsername = image.getReview().getCustomer().getAccount().getEmail();
            System.out.println(ownerUsername);
            if (!currentUsername.equals(ownerUsername)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Bạn không có quyền xóa ảnh này. Ảnh thuộc về người dùng khác.");
            }
        }

        // Xóa ảnh trên Cloudinary
        try {
            cloudinaryService.deleteFileByUrl(image.getSrc());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi khi xóa ảnh trên Cloudinary: " + e.getMessage());
        }

        // Xóa bản ghi trong DB
        reviewImageRepository.delete(image);
    }
}
