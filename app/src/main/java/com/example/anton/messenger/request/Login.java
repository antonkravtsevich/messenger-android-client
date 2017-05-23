package com.example.anton.messenger.request;

/**
 * Created by anton on 15.5.17.
 */

public class Login {
    String username;
    String password;

    public String getLogin() {
        return username;
    }

    public void setLogin(String login) {
        this.username = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Login() {
    }

    public Login(String login, String password) {
        this.username = login;
        this.password = password;
    }
}
