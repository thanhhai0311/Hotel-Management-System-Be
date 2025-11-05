package com.javaweb.model.dto.ReviewDTO;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class ReviewUpdateDTO {
	private String details;
	private Integer star;
	private String type;
	private Integer idCustomer;
	private List<Integer> imageIdsToDelete; // danh sách ID ảnh cần xóa
	private List<MultipartFile> images; // danh sách ảnh mới cần thêm

	// Getters & Setters
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(Integer idCustomer) {
		this.idCustomer = idCustomer;
	}

	public List<Integer> getImageIdsToDelete() {
		return imageIdsToDelete;
	}

	public void setImageIdsToDelete(List<Integer> imageIdsToDelete) {
		this.imageIdsToDelete = imageIdsToDelete;
	}

	public List<MultipartFile> getImages() {
		return images;
	}

	public void setImages(List<MultipartFile> images) {
		this.images = images;
	}
}
