package com.example.smartlab.Models;

public class CategoriesAnalyzes {
    private int id_categories_of_analyses;
    private String title;
    private String description;
    private int id_stocks;
    private String icons;

    public int getId_categories_of_analyses() {
        return id_categories_of_analyses;
    }

    public void setId_categories_of_analyses(int id_categories_of_analyses) {
        this.id_categories_of_analyses = id_categories_of_analyses;
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

    public int getId_stocks() {
        return id_stocks;
    }

    public void setId_stocks(int id_stocks) {
        this.id_stocks = id_stocks;
    }

    public String getIcons() {
        return icons;
    }

    public void setIcons(String icons) {
        this.icons = icons;
    }
}
