package com.example.anton.messenger.response.user;

/**
 * Created by anton on 22.5.17.
 */

public class PersonalData {
    private String first_name;
    private String last_name;
    private String email;

    public PersonalData(String firstName, String lastName, String email) {
        this.first_name = firstName;
        this.last_name = lastName;
        this.email = email;
    }

    public PersonalData() {
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String lastName) {
        this.last_name = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
