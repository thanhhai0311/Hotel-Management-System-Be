package com.javaweb.model.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RoomStatusRequest {
    @NotBlank(message = "Trạng thái không được để trống")
    @Size(max = 100, message = "Tên trạng thái tối đa 100 ký tự")
    private String name;

    @Size(max = 1000, message = "Chi tiết tối đa 1000 ký tự")
    private String details;

    // Getters/Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}