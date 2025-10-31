package com.javaweb.model.dto.AccountDTO;

import lombok.Data;

@Data
public class AccountDTO {
    private Integer id;
    private String email;
    private boolean active;
    private String roleName;
    
    public AccountDTO() {}

    public AccountDTO(Integer id, String email, boolean active, String roleName) {
        this.id = id;
        this.email = email;
        this.active = active;
        this.roleName = roleName;
    }
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
    
    
}