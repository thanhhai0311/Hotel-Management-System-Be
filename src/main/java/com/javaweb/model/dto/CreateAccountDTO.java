package com.javaweb.model.dto;

public class CreateAccountDTO {
	private String email;
    private String password;
    private Integer idRole;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIdRole() {
        return idRole;
    }
    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }
}
