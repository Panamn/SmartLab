package com.example.smartlab.Models;

import java.util.Date;

public class ProfileUpdate {
    private String full_name;
    private int role;


    public ProfileUpdate(String full_name, int role) {
        this.full_name = full_name;
        this.role = role;

    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

}
