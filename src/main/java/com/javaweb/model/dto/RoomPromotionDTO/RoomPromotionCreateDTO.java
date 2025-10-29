package com.javaweb.model.dto.RoomPromotionDTO;

import java.util.Date;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.format.annotation.DateTimeFormat;

public class RoomPromotionCreateDTO {

    private String name;
    private String details;
    private Float discount;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date endTime;

    private MultipartFile banner;
    private List<Integer> roomIds;

    private Boolean isActive; // ✅ có thể gửi từ client hoặc để backend xử lý

    // === Getter & Setter ===
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public Float getDiscount() { return discount; }
    public void setDiscount(Float discount) { this.discount = discount; }

    public Date getStartTime() { return startTime; }
    public void setStartTime(Date startTime) { this.startTime = startTime; }

    public Date getEndTime() { return endTime; }
    public void setEndTime(Date endTime) { this.endTime = endTime; }

    public MultipartFile getBanner() { return banner; }
    public void setBanner(MultipartFile banner) { this.banner = banner; }

    public List<Integer> getRoomIds() { return roomIds; }
    public void setRoomIds(List<Integer> roomIds) { this.roomIds = roomIds; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
