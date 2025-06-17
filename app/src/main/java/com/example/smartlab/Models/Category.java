package com.example.smartlab.Models;


public class Category {
    private int id_categories_of_analyses;
    private String title;

    public Category(int id_categories_of_analyses, String title) {
        this.id_categories_of_analyses = id_categories_of_analyses;
        this.title = title;
    }


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
}