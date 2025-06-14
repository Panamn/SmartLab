package com.example.smartlab.Models;

import com.google.gson.annotations.SerializedName;

public class Doctors{
    @SerializedName("specialization_categories")
    private Specialization specializationCategories;
    @SerializedName("id_specialization")
    private String  id_specialization;
    private int experience;
    private float rating;
    private int price_of_admission;
    private String  about_doctor;
    @SerializedName("id_profile")
    private String id_profile;
    @SerializedName("profiles")
    private Profiles profile;

    public Doctors(Specialization specializationCategories, String id_specialization, int experience, float rating, int price_of_admission, String about_doctor, String id_profile, Profiles profile) {
        this.specializationCategories = specializationCategories;
        this.id_specialization = id_specialization;
        this.experience = experience;
        this.rating = rating;
        this.price_of_admission = price_of_admission;
        this.about_doctor = about_doctor;
        this.id_profile = id_profile;
        this.profile = profile;
    }


    public Specialization getSpecializationCategories() {
        return specializationCategories;
    }

    public void setSpecializationCategories(Specialization specializationCategories) {
        this.specializationCategories = specializationCategories;
    }

    public String getId_specialization() {
        return id_specialization;
    }

    public void setId_specialization(String id_specialization) {
        this.id_specialization = id_specialization;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getPrice_of_admission() {
        return price_of_admission;
    }

    public void setPrice_of_admission(int price_of_admission) {
        this.price_of_admission = price_of_admission;
    }

    public String getAbout_doctor() {
        return about_doctor;
    }

    public void setAbout_doctor(String about_doctor) {
        this.about_doctor = about_doctor;
    }

    public String getId_profile() {
        return id_profile;
    }

    public void setId_profile(String id_profile) {
        this.id_profile = id_profile;
    }

    public Profiles getProfile() {
        return profile;
    }

    public void setProfile(Profiles profile) {
        this.profile = profile;
    }
}