package com.example.smartlab.Models;

import com.google.gson.annotations.SerializedName;

public class ProfileUpdateCard {
    @SerializedName("full_name")
    public String full_name;
    @SerializedName("sate_of_birth")
    public String sate_of_birth;
    @SerializedName("gender")
    public String gender;

    public ProfileUpdateCard(String full_name, String sate_of_birth, String gender) {
        this.full_name = full_name;
        this.sate_of_birth = sate_of_birth;
        this.gender = gender;
    }

}
