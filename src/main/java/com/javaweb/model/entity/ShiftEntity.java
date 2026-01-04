package com.javaweb.model.entity;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "shift")
public class ShiftEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    // Lưu giờ bắt đầu của ca
    @Column(name = "startTime", nullable = false)
    private LocalTime startTime;

    // Lưu giờ kết thúc của ca
    @Column(name = "endTime", nullable = false)
    private LocalTime endTime;

    @Column(name = "details")
    private String details;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "shift")
    private List<ShiftingEntity> shiftings;

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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<ShiftingEntity> getShiftings() {
        return shiftings;
    }

    public void setShiftings(List<ShiftingEntity> shiftings) {
        this.shiftings = shiftings;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }


}
