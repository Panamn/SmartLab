package com.example.smartlab.Models;

public class AddNotificationRecords {
    private String title;
    private String text;
    private String period_of_relevance;
    private String id_profile;
    private String name_doctors;
    private String notification_type;

    public AddNotificationRecords(String title, String text, String period_of_relevance, String id_profile, String name_doctors, String notification_type) {
        this.title = title;
        this.text = text;
        this.period_of_relevance = period_of_relevance;
        this.id_profile = id_profile;
        this.name_doctors = name_doctors;
        this.notification_type = notification_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPeriod_of_relevance() {
        return period_of_relevance;
    }

    public void setPeriod_of_relevance(String period_of_relevance) {
        this.period_of_relevance = period_of_relevance;
    }

    public String getId_profile() {
        return id_profile;
    }

    public void setId_profile(String id_profile) {
        this.id_profile = id_profile;
    }

    public String getName_doctors() {
        return name_doctors;
    }

    public void setName_doctors(String name_doctors) {
        this.name_doctors = name_doctors;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }
}
