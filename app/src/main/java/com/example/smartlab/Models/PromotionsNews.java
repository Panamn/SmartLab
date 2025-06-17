package com.example.smartlab.Models;

public class PromotionsNews {
    private String title;
    private String description;
    private float discount;
    private String photo;
    private String background;

    public PromotionsNews(String title, String description, float discount, String photo, String background) {
        this.title = title;
        this.description = description;
        this.discount = discount;
        this.photo = photo;
        this.background = background;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
