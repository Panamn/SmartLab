package com.example.smartlab.Models;

import java.util.Date;

public class Profile {
    private String id;
    private String full_name;
    private String avatar_url;
    private String sate_of_birth;
    private String phone;
    private int role;
    private String gender;
    private String address;


    public Profile(String id, String full_name, String avatar_url, String sate_of_birth, String phone, int role, String gender, String address) {
        this.id = id;
        this.full_name = full_name;
        this.avatar_url = avatar_url;
        this.sate_of_birth = sate_of_birth;
        this.phone = phone;
        this.role = role;
        this.gender = gender;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getSate_of_birth() {
        return sate_of_birth;
    }

    public void setSate_of_birth(String sate_of_birth) {
        this.sate_of_birth = sate_of_birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
