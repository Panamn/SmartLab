package com.example.smartlab.Models;

public class ProfileUpdateCard {
    public String full_name;
    //  public String avatar_url;
    public String sate_of_birth;
    public String gender;

    public ProfileUpdateCard(String full_name, String sate_of_birth, String gender) {
        this.full_name = full_name;
        this.sate_of_birth = sate_of_birth;
        this.gender = gender;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getSate_of_birth() {
        return sate_of_birth;
    }

    public void setSate_of_birth(String sate_of_birth) {
        this.sate_of_birth = sate_of_birth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
