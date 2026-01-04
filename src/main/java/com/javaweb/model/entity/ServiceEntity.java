package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "service")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String details;

    @Column(nullable = false)
    private Float price;

    @Column(nullable = false)
    private Integer isAvaiable;

    @Column(nullable = false)
    private String unit;
//    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "idServiceCategory", nullable = false)
    private ServiceCategoryEntity serviceCategory;

    @ManyToOne
    @JoinColumn(name = "idHotel", nullable = false)
    private HotelEntity hotel;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceImageEntity> serviceImages;

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

    public List<BookingServiceEntity> getBookingServices() {
        return bookingServices;
    }

    public void setBookingServices(List<BookingServiceEntity> bookingServices) {
        this.bookingServices = bookingServices;
    }

    public HotelEntity getHotel() {
        return hotel;
    }

    public void setHotel(HotelEntity hotel) {
        this.hotel = hotel;
    }

}
