package com.example.smartlab.Models;

public class SpecializationCategories {
    private int id_specialization_categories;
    private String title;
    private String description;
    private String background;
    private String icons;

    public SpecializationCategories(int id_specialization_categories, String title, String description, String background, String icons) {
        this.id_specialization_categories = id_specialization_categories;
        this.title = title;
        this.description = description;
        this.background = background;
        this.icons = icons;
    }

    public int getId_specialization_categories() {
        return id_specialization_categories;
    }

    public void setId_specialization_categories(int id_specialization_categories) {
        this.id_specialization_categories = id_specialization_categories;
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

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }
}
