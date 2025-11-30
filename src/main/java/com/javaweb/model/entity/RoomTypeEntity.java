package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roomtype")
public class RoomTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String details;

    private Integer bedCount;

    private Integer maxOccupancy;

    private Float price;

    private Float area;

    private Boolean isPrivateBathroom = false;

    private Boolean isFreeToiletries = false;

    private Boolean isAirConditioning = false;

    private Boolean isSoundproofing = false;

    private Boolean isTV = false;

    private Boolean isMiniBar = false;

    private Boolean isWorkDesk = false;

    private Boolean isSeatingArea = false;

    private Boolean isSafetyFeatures = false;

    private Boolean isSmoking = false;

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TypeImageEntity> typeImages = new ArrayList<>();

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomEntity> rooms = new ArrayList<>();

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

    public Integer getBedCount() {
        return bedCount;
    }

    public void setBedCount(Integer bedCount) {
        this.bedCount = bedCount;
    }

    public Integer getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(Integer maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Float getArea() {
        return area;
    }

    public void setArea(Float area) {
        this.area = area;
    }

    public Boolean getIsPrivateBathroom() {
        return isPrivateBathroom;
    }

    public void setIsPrivateBathroom(Boolean isPrivateBathroom) {
        this.isPrivateBathroom = isPrivateBathroom;
    }

    public Boolean getIsFreeToiletries() {
        return isFreeToiletries;
    }

    public void setIsFreeToiletries(Boolean isFreeToiletries) {
        this.isFreeToiletries = isFreeToiletries;
    }

    public Boolean getIsAirConditioning() {
        return isAirConditioning;
    }

    public void setIsAirConditioning(Boolean isAirConditioning) {
        this.isAirConditioning = isAirConditioning;
    }

    public Boolean getIsSoundproofing() {
        return isSoundproofing;
    }

    public void setIsSoundproofing(Boolean isSoundproofing) {
        this.isSoundproofing = isSoundproofing;
    }

    public Boolean getIsTV() {
        return isTV;
    }

    public void setIsTV(Boolean isTV) {
        this.isTV = isTV;
    }

    public Boolean getIsMiniBar() {
        return isMiniBar;
    }

    public void setIsMiniBar(Boolean isMiniBar) {
        this.isMiniBar = isMiniBar;
    }

    public Boolean getIsWorkDesk() {
        return isWorkDesk;
    }

    public void setIsWorkDesk(Boolean isWorkDesk) {
        this.isWorkDesk = isWorkDesk;
    }

    public Boolean getIsSeatingArea() {
        return isSeatingArea;
    }

    public void setIsSeatingArea(Boolean isSeatingArea) {
        this.isSeatingArea = isSeatingArea;
    }

    public Boolean getIsSafetyFeatures() {
        return isSafetyFeatures;
    }

    public void setIsSafetyFeatures(Boolean isSafetyFeatures) {
        this.isSafetyFeatures = isSafetyFeatures;
    }

    public Boolean getIsSmoking() {
        return isSmoking;
    }

    public void setIsSmoking(Boolean isSmoking) {
        this.isSmoking = isSmoking;
    }

    public List<TypeImageEntity> getTypeImages() {
        return typeImages;
    }

    public void setTypeImages(List<TypeImageEntity> typeImages) {
        this.typeImages = typeImages;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setRooms(List<RoomEntity> rooms) {
        this.rooms = rooms;
    }

    public List<RoomEntity> getRooms() {
        return rooms;
    }
}
