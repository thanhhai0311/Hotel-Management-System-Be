package com.javaweb.model.dto.ServiceCategoryDTO;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class ServiceUpdateDTO {
    private String name;
    private String details;
    private Float price;
    private Integer isAvaiable;
    private String unit;
    private Integer quantity;
    private Integer idHotel;
    private Integer idCategory;

    private List<Integer> imageIdsToDelete; 
    private List<MultipartFile> images;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Integer getIsAvaiable() {
		return isAvaiable;
	}
	public void setIsAvaiable(Integer isAvaiable) {
		this.isAvaiable = isAvaiable;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Integer getIdHotel() {
		return idHotel;
	}
	public void setIdHotel(Integer idHotel) {
		this.idHotel = idHotel;
	}
	public Integer getIdCategory() {
		return idCategory;
	}
	public void setIdCategory(Integer idCategory) {
		this.idCategory = idCategory;
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
