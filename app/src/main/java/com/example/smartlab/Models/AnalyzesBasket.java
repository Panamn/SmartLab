package com.example.smartlab.Models;

public class AnalyzesBasket {
    private int id_analyzes;
    private float price;

    public AnalyzesBasket(int id_analyzes, float price) {
        this.id_analyzes = id_analyzes;
        this.price = price;
    }

    public int getId_analyzes() {
        return id_analyzes;
    }

    public void setId_analyzes(int id_analyzes) {
        this.id_analyzes = id_analyzes;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
