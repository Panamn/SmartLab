package com.example.smartlab.Models;

import java.util.Date;

public class Profile {
    private String id;
    private String full_name;
    private String avatar_url;
    private Date sate_of_birth;
    private String phone;
    private Role role;
    private String gender;

    public Profile(String id, String full_name, String avatar_url, Date sate_of_birth, String phone, Role role, String gender) {
        this.id = id;
        this.full_name = full_name;
        this.avatar_url = avatar_url;
        this.sate_of_birth = sate_of_birth;
        this.phone = phone;
        this.role = role;
        this.gender = gender;
    }
}
