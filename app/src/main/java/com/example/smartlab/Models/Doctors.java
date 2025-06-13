package com.example.smartlab.Models;

public class Doctors{
    public int id_specialization;
    public int experience;
    public float rating;
    public int price_of_admission;
    public String  about_doctor;
    public String id_profile;

    public int getId_specialization() {
        return id_specialization;
    }

    public void setId_specialization(int id_specialization) {
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
}