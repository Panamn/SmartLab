package com.example.smartlab.Models;

import com.google.gson.annotations.SerializedName;

public class OrderId {
    @SerializedName("id_analysis_orders")
    private int id;

    public OrderId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
