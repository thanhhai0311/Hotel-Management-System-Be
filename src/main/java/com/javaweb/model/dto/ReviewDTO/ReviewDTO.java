package com.javaweb.model.dto.ReviewDTO;

import java.util.Date;
import java.util.List;

import com.javaweb.model.dto.ReviewImageDTO.ReviewImageDTO;

public class ReviewDTO {
    private Integer id;
    private String details;
    private Integer star;
    private String type;
    private Date day;
    private Integer customerId;
    private List<ReviewImageDTO> reviewImages;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public Date getDay() {
		return day;
	}
	public void setDay(Date day) {
		this.day = day;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public List<ReviewImageDTO> getReviewImages() {
		return reviewImages;
	}
	public void setReviewImages(List<ReviewImageDTO> reviewImages) {
		this.reviewImages = reviewImages;
	}
    
    
}
