package com.javaweb.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    // 1. Cấu hình giới hạn: Chỉ cho phép ảnh (JPG, PNG...)
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/gif", "image/webp");

    // 2. Cấu hình giới hạn: File tối đa 10MB (10 * 1024 * 1024 bytes)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    public CloudinaryService(@Value("${cloudinary.cloud_name}") String cloudName,
                             @Value("${cloudinary.api_key}") String apiKey, @Value("${cloudinary.api_secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(
                ObjectUtils.asMap("cloud_name", cloudName, "api_key", apiKey, "api_secret", apiSecret, "secure", true));
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File không được để trống.");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_EXTENSIONS.contains(contentType)) {
            throw new IllegalArgumentException("Định dạng file không hợp lệ. Chỉ chấp nhận ảnh (JPG, PNG, GIF, WebP).");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File quá lớn. Dung lượng tối đa cho phép là 5MB.");
        }
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "hotel_images"));
        return uploadResult.get("secure_url").toString();
    }

    public void deleteFile(String publicId) throws IOException {
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public void deleteFileByUrl(String imageUrl) {
        try {
            String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
            String folderPath = "hotel_images/" + fileName;

            Map result = cloudinary.uploader().destroy(folderPath, ObjectUtils.emptyMap());
            System.out.println("Deleted image: " + folderPath + " => " + result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể xóa ảnh Cloudinary: " + e.getMessage());
        }
    }
}
