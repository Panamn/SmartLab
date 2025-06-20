package com.example.smartlab.Models;

public class Notification {
    private int id_notifications;
    private String title;
    private String text;
    private String period_of_relevance;
    private String date_of_creation;
    private String id_profile;
    private String name_doctors;
    private String notification_type;
    private float sum;


    public Notification(int id_notifications, String title, String text, String period_of_relevance, String date_of_creation, String id_profile, String name_doctors, String notification_type, float sum) {
        this.id_notifications = id_notifications;
        this.title = title;
        this.text = text;
        this.period_of_relevance = period_of_relevance;
        this.date_of_creation = date_of_creation;
        this.id_profile = id_profile;
        this.name_doctors = name_doctors;
        this.notification_type = notification_type;
        this.sum = sum;
    }

    public int getId_notifications() {
        return id_notifications;
    }

    public void setId_notifications(int id_notifications) {
        this.id_notifications = id_notifications;
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

    public String getDate_of_creation() {
        return date_of_creation;
    }

    public void setDate_of_creation(String date_of_creation) {
        this.date_of_creation = date_of_creation;
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

    public float getSum() {
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }
}
