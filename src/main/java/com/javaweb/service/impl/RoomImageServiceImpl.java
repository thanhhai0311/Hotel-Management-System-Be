package com.javaweb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.model.entity.RoomImageEntity;
import com.javaweb.repository.RoomImageRepository;
import com.javaweb.service.CloudinaryService;
import com.javaweb.service.RoomImageService;

@Service
public class RoomImageServiceImpl implements RoomImageService {

    @Autowired
    private RoomImageRepository roomImageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public void deleteRoomImageBySrc(String src) {
        // Tìm ảnh trong DB theo src
        RoomImageEntity image = roomImageRepository.findBySrc(src)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy ảnh có src = " + src));
        
        try {
            cloudinaryService.deleteFileByUrl(image.getSrc());
//            log.info("✅ Đã xóa ảnh khỏi Cloudinary: {}", image.getSrc());
        } catch (Exception e) {
//            log.error("❌ Lỗi khi xóa ảnh trên Cloudinary: {}", e.getMessage(), e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi khi xóa ảnh trên Cloudinary: " + e.getMessage()
            );
        }

        // Lấy id từ ảnh vừa tìm được
        Integer imageId = image.getId();

        // Xóa ảnh theo id (gọi sang hàm deleteRoomImageById)
        deleteRoomImageById(imageId);
    }

    /**
     * Xóa ảnh dựa trên id (được gọi nội bộ hoặc từ controller)
     */
    @Override
    @Transactional
    public void deleteRoomImageById(Integer id) {
        // Kiểm tra xem ảnh có tồn tại không
        RoomImageEntity image = roomImageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Không tìm thấy ảnh có ID = " + id));

        // Xóa ảnh trên Cloudinary
        try {
            cloudinaryService.deleteFileByUrl(image.getSrc());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi khi xóa ảnh trên Cloudinary: " + e.getMessage());
        }

        // Xóa bản ghi ảnh trong DB
        roomImageRepository.delete(image);
    }
}
