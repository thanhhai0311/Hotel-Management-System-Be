package com.javaweb.service.impl;

import com.javaweb.model.entity.CustomerIdentificationEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.repository.CustomerIdentificationRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.CloudinaryService;
import com.javaweb.service.CustomerIdentificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerIdentificationServiceImpl implements CustomerIdentificationService {

    @Autowired
    private CustomerIdentificationRepository identificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public void processCheckInImage(Integer customerId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return;

        UserEntity customer = userRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại"));

        String newImageUrl = cloudinaryService.uploadFile(file);

        Optional<CustomerIdentificationEntity> opt = identificationRepository.findByCustomer_Id(customerId);

        CustomerIdentificationEntity entity;

        if (opt.isPresent()) {
            entity = opt.get();

            if (entity.getIdentificationImage() != null) {
                try {
                    cloudinaryService.deleteFileByUrl(entity.getIdentificationImage());
                } catch (Exception e) {
                    System.err.println("Lỗi xóa ảnh cũ: " + e.getMessage());
                }
            }
        } else {
            entity = new CustomerIdentificationEntity();
            entity.setCustomer(customer);
        }

        entity.setIdentificationImage(newImageUrl);
        entity.setExpiryDate(null);

        identificationRepository.save(entity);
    }

    @Override
    @Transactional
    public void processCheckOutExpiry(Integer customerId) {
        Optional<CustomerIdentificationEntity> opt = identificationRepository.findByCustomer_Id(customerId);

        if (opt.isPresent()) {
            CustomerIdentificationEntity entity = opt.get();
            entity.setExpiryDate(LocalDateTime.now().plusDays(30));
            identificationRepository.save(entity);
        }
    }

    @Override
    @Transactional
    public void cleanupExpiredImages() {
        LocalDateTime now = LocalDateTime.now();
        List<CustomerIdentificationEntity> expiredList = identificationRepository.findByExpiryDateBefore(now);

        if (expiredList.isEmpty()) return;

        System.out.println("--- Scheduler: Tìm thấy " + expiredList.size() + " ảnh hết hạn ---");

        for (CustomerIdentificationEntity entity : expiredList) {
            String url = entity.getIdentificationImage();

            if (url != null && !url.isEmpty()) {
                try {
                    cloudinaryService.deleteFileByUrl(url);
                    System.out.println("Đã xóa ảnh Cloud: " + url);
                } catch (Exception e) {
                    System.err.println("Lỗi xóa ảnh Cloud: " + e.getMessage());
                }
            }

            entity.setIdentificationImage(null);
            entity.setExpiryDate(null);
        }

        identificationRepository.saveAll(expiredList);
    }
}