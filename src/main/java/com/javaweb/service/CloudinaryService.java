package com.javaweb.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

	private final Cloudinary cloudinary;

	public CloudinaryService(@Value("${cloudinary.cloud_name}") String cloudName,
			@Value("${cloudinary.api_key}") String apiKey, @Value("${cloudinary.api_secret}") String apiSecret) {
		this.cloudinary = new Cloudinary(
				ObjectUtils.asMap("cloud_name", cloudName, "api_key", apiKey, "api_secret", apiSecret, "secure", true));
	}

	public String uploadFile(MultipartFile file) throws IOException {
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
