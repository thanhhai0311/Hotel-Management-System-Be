package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Service")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String details;
    private Float price;
    private Integer isAvaiable;
    private String unit;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "idServiceCategory")
    private ServiceCategoryEntity serviceCategory;

    @OneToMany(mappedBy = "service")
    private List<ServiceImageEntity> serviceImages;

    @OneToMany(mappedBy = "service")
    private List<ServicePromotionEntity> servicePromotions;

    @OneToMany(mappedBy = "service")
    private List<BookingServiceEntity> bookingServices;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public ServiceCategoryEntity getServiceCategory() {
		return serviceCategory;
	}

	public void setServiceCategory(ServiceCategoryEntity serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	public List<ServiceImageEntity> getServiceImages() {
		return serviceImages;
	}

	public void setServiceImages(List<ServiceImageEntity> serviceImages) {
		this.serviceImages = serviceImages;
	}

	public List<ServicePromotionEntity> getServicePromotions() {
		return servicePromotions;
	}

	public void setServicePromotions(List<ServicePromotionEntity> servicePromotions) {
		this.servicePromotions = servicePromotions;
	}

	public List<BookingServiceEntity> getBookingServices() {
		return bookingServices;
	}

	public void setBookingServices(List<BookingServiceEntity> bookingServices) {
		this.bookingServices = bookingServices;
	}

    
}
