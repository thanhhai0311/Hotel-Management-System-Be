package com.javaweb.model.dto.response;

public class RoomTypeResponse {
    private Integer id;
    private String name;
    private String details;

    public RoomTypeResponse() {}
    public RoomTypeResponse(Integer id, String name, String details) {
        this.id = id; this.name = name; this.details = details;
    }

    // Getters/Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}