package com.javaweb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.model.entity.TypeImageEntity;
import com.javaweb.repository.TypeImageRepository;
import com.javaweb.service.CloudinaryService;
import com.javaweb.service.TypeImageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TypeImageServiceImpl implements TypeImageService {

    @Autowired
    private TypeImageRepository typeImageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    /**
     * Xóa ảnh loại phòng theo src
     */
    @Override
    @Transactional
    public void deleteTypeImageBySrc(String src) {
//        log.info("🧹 Bắt đầu xóa ảnh loại phòng theo src: {}", src);

        // Tìm ảnh trong DB
        TypeImageEntity image = typeImageRepository.findBySrc(src)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy ảnh loại phòng có src = " + src));

        // Xóa ảnh trên Cloudinary
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
        typeImageRepository.delete(image);
//        log.info("🗑️ Đã xóa ảnh loại phòng khỏi DB (ID: {}, src: {})", image.getId(), image.getSrc());
    }

    /**
     * Xóa ảnh loại phòng theo ID
     */
    @Override
    @Transactional
    public void deleteTypeImageById(Integer id) {
//        log.info("🧹 Bắt đầu xóa ảnh loại phòng theo ID: {}", id);

        // Tìm ảnh trong DB
        TypeImageEntity image = typeImageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy ảnh loại phòng có ID = " + id));

        // Xóa ảnh trên Cloudinary
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
        typeImageRepository.delete(image);
//        log.info("🗑️ Đã xóa ảnh loại phòng khỏi DB (ID: {}, src: {})", image.getId(), image.getSrc());
    }
}
