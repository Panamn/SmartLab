package com.example.smartlab.Models;

import com.google.gson.annotations.SerializedName;

public class AddNotificationBuyAnalyzes {
    @SerializedName("title")
    private String title;
    @SerializedName("text")
    private String text;
    @SerializedName("period_of_relevance")
    private String period_of_relevance;
    @SerializedName("id_profile")
    private String id_profile;
    @SerializedName("notification_type")
    private String notification_type;
    @SerializedName("sum")
    private float sum;

    public AddNotificationBuyAnalyzes(String title, String text, String period_of_relevance, String id_profile, String notification_type, float sum) {
        this.title = title;
        this.text = text;
        this.period_of_relevance = period_of_relevance;
        this.id_profile = id_profile;
        this.notification_type = notification_type;
        this.sum = sum;
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
