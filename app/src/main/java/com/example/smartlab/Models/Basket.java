package com.example.smartlab.Models;

import com.google.gson.annotations.SerializedName;

public class Basket {
    private int id_additions;
    @SerializedName("id_analyzes")
    private int id_analyzes;
    @SerializedName("analyzes")
    private Analyzes analyzes;
    private String id_client;

    public Basket(int id_additions, int id_analyzes, Analyzes analyzes, String id_client) {
        this.id_additions = id_additions;
        this.id_analyzes = id_analyzes;
        this.analyzes = analyzes;
        this.id_client = id_client;
    }

    public int getId_additions() {
        return id_additions;
    }

    public void setId_additions(int id_additions) {
        this.id_additions = id_additions;
    }

    public int getId_analyzes() {
        return id_analyzes;
    }

    public void setId_analyzes(int id_analyzes) {
        this.id_analyzes = id_analyzes;
    }

    public Analyzes getAnalyzes() {
        return analyzes;
    }

    public void setAnalyzes(Analyzes analyzes) {
        this.analyzes = analyzes;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }
}
