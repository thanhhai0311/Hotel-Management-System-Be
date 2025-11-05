package com.javaweb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.model.entity.ServiceImageEntity;
import com.javaweb.repository.ServiceImageRepository;
import com.javaweb.service.CloudinaryService;
import com.javaweb.service.ServiceImageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceImageServiceImpl implements ServiceImageService {

    @Autowired
    private ServiceImageRepository serviceImageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    /**
     * Xóa ảnh dịch vụ theo src
     */
    @Override
    @Transactional
    public void deleteServiceImageBySrc(String src) {
//        log.info("🧹 Bắt đầu xóa ảnh dịch vụ theo src: {}", src);

        // Tìm ảnh trong DB
        ServiceImageEntity image = serviceImageRepository.findBySrc(src)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy ảnh dịch vụ có src = " + src));

        // Xóa ảnh khỏi Cloudinary
        try {
            cloudinaryService.deleteFileByUrl(image.getSrc());
//            log.info("✅ Đã xóa ảnh khỏi Cloudinary: {}", image.getSrc());
        } catch (Exception e) {
//            log.error("❌ Lỗi khi xóa ảnh trên Cloudinary: {}", e.getMessage(), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi khi xóa ảnh trên Cloudinary: " + e.getMessage());
        }

        // Xóa bản ghi khỏi DB
        serviceImageRepository.delete(image);
//        log.info("🗑️ Đã xóa ảnh dịch vụ khỏi DB (ID: {}, src: {})", image.getId(), image.getSrc());
    }

    /**
     * Xóa ảnh dịch vụ theo ID
     */
    @Override
    @Transactional
    public void deleteServiceImageById(Integer id) {
//        log.info("🧹 Bắt đầu xóa ảnh dịch vụ theo ID: {}", id);

        // Tìm ảnh theo ID
        ServiceImageEntity image = serviceImageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy ảnh dịch vụ có ID = " + id));

        // Xóa ảnh khỏi Cloudinary
        try {
            cloudinaryService.deleteFileByUrl(image.getSrc());
//            log.info("✅ Đã xóa ảnh khỏi Cloudinary: {}", image.getSrc());
        } catch (Exception e) {
//            log.error("❌ Lỗi khi xóa ảnh trên Cloudinary: {}", e.getMessage(), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi khi xóa ảnh trên Cloudinary: " + e.getMessage());
        }

        // Xóa khỏi DB
        serviceImageRepository.delete(image);
//        log.info("🗑️ Đã xóa ảnh dịch vụ khỏi DB (ID: {}, src: {})", image.getId(), image.getSrc());
    }
}
