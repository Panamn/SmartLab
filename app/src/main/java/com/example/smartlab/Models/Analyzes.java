package com.example.smartlab.Models;

public class Analyzes {
    private int id_analyzes;
    private int id_categories_of_analyses;
    private String title;
    private String preparation;
    private String biomaterial;
    private String description;
    private int price;
    private int id_stocks;
    private int period_of_execution;


    public Analyzes(int id_analyzes, int id_categories_of_analyses, String title, String preparation, String biomaterial, String description, int price, int id_stocks, int period_of_execution) {
        this.id_analyzes = id_analyzes;
        this.id_categories_of_analyses = id_categories_of_analyses;
        this.title = title;
        this.preparation = preparation;
        this.biomaterial = biomaterial;
        this.description = description;
        this.price = price;
        this.id_stocks = id_stocks;
        this.period_of_execution = period_of_execution;
    }

    public int getId_analyzes() {
        return id_analyzes;
    }

    public void setId_analyzes(int id_analyzes) {
        this.id_analyzes = id_analyzes;
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

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public String getBiomaterial() {
        return biomaterial;
    }

    public void setBiomaterial(String biomaterial) {
        this.biomaterial = biomaterial;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId_stocks() {
        return id_stocks;
    }

    public void setId_stocks(int id_stocks) {
        this.id_stocks = id_stocks;
    }

    public int getPeriod_of_execution() {
        return period_of_execution;
    }

    public void setPeriod_of_execution(int period_of_execution) {
        this.period_of_execution = period_of_execution;
    }
}
