package com.example.anton.messenger.response.user;

import java.util.ArrayList;

/**
 * Created by anton on 22.5.17.
 */

public class UsersResponse {
    private ArrayList<User> users;
    private String status;

    public UsersResponse(ArrayList<User> users, String status) {
        this.users = users;
        this.status = status;
    }

    public UsersResponse() {
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
