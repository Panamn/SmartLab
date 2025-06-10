package com.example.smartlab.Models;

import java.util.Date;

public class ProfileUpdate {
    private String full_name;


    public ProfileUpdate( String full_name) {
        this.full_name = full_name;

//        this.role = role;

    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }



//    public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }

}
