package com.example.smartlab.Models;

import com.google.gson.annotations.SerializedName;

public class UpdateBasket {
        private int id_analyzes;
        private String id_client;

    public UpdateBasket(int id_analyzes, String id_client) {
        this.id_analyzes = id_analyzes;
        this.id_client = id_client;
    }

    public int getId_analyzes() {
        return id_analyzes;
    }

    public void setId_analyzes(int id_analyzes) {
        this.id_analyzes = id_analyzes;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }
}
