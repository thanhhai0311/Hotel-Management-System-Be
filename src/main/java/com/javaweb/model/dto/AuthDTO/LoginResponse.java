package com.javaweb.model.dto.AuthDTO;

public class LoginResponse {
    private String token;
    private String role;
    private int idUser;

    public LoginResponse(String token, String role, int idUser) {
        super();
        this.token = token;
        this.role = role;
        this.idUser = idUser;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}
