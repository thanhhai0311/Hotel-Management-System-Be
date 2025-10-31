package com.javaweb.model.dto.UserDTO;

import java.util.Date;

public class UserResponse {
    private Integer id;
    private String name;
    private String phone;
    private String gender;
    private String address;
    private Date dob;
    private String username;
    private String roleName;

    public UserResponse() {}

    public UserResponse(Integer id, String name, String phone, String gender, String address, Date dob,
                        String username, String roleName) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.dob = dob;
        this.username = username;
        this.roleName = roleName;
    }

    // Getter & Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
}