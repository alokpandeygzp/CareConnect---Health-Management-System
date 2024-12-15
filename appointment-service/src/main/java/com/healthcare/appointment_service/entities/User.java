package com.healthcare.appointment_service.entities;

public class User {

    private String name;
    private String email;
    private String password;
    private String about;
    private String status;

    public User(String name, String email, String password, String about, String status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.about = about;
        this.status = status;
    }

    public User() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
