package com.example.anton.messenger.response;

/**
 * Created by anton on 22.5.17.
 */

public class LoginResponse {
    private String status;
    private String jwt;

    public LoginResponse(String status, String jwt) {
        this.status = status;
        this.jwt = jwt;
    }

    public LoginResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
