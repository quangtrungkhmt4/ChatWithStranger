package com.example.quang.chatwithstranger.model;

public class User {
    private int id;
    private String user;
    private String pass;
    private String email;
    private String name;
    private String phone;
    private int gender;
    private int isActive;
    private String createdAt;

    public User(int id, String user, String pass, String email, String name, String phone, int gender, int isActive, String createdAt) {
        this.id = id;
        this.user = user;
        this.pass = pass;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
