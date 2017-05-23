package com.example.anton.messenger.response.user;

import java.util.ArrayList;

import static java.lang.System.in;

/**
 * Created by anton on 22.5.17.
 */

public class User {
    private String username;
    private String _id;
    private PersonalData pd;

    public User() {
    }

    public User(String username, String _id, PersonalData pd) {
        this.username = username;
        this._id = _id;
        this.pd = pd;
    }

    public static ArrayList<User> FilterUsers(ArrayList<User> users, String filter_string){
        ArrayList<User> res = new ArrayList<>();
        for(User user: users){
            String username = user.getPd().getFirstName()+" "+user.getPd().getLastName();
            if(username.indexOf(filter_string)==0){
                res.add(user);
            }
        }
        return res;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public PersonalData getPd() {
        return pd;
    }

    public void setPd(PersonalData pd) {
        this.pd = pd;
    }

    @Override
    public String toString() {
        return "User " + _id + "{" +
                "username='" + username + '\'' +
                ", pd=" + pd +
                '}';
    }
}
