package com.example.anton.messenger.response.user_data;

import com.example.anton.messenger.response.user.User;

/**
 * Created by anton on 22.5.17.
 */

public class UserDataResponse {
    private User user;
    private String status;

    public UserDataResponse() {
    }

    public UserDataResponse(User user, String status) {
        this.user = user;
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
