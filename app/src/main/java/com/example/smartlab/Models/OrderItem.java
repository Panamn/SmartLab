package com.example.smartlab.Models;

public class OrderItem {
    private int id_order;
    private int id_analyzes;

    public OrderItem(int id_order, int id_analyzes) {
        this.id_order = id_order;
        this.id_analyzes = id_analyzes;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public int getId_analyzes() {
        return id_analyzes;
    }

    public void setId_analyzes(int id_analyzes) {
        this.id_analyzes = id_analyzes;
    }
}
