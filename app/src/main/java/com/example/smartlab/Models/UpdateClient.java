package com.example.smartlab.Models;

import com.google.gson.annotations.SerializedName;

public class UpdateClient {
    @SerializedName("id_client")
    private String id_client;

    public UpdateClient(String id_client) {
        this.id_client = id_client;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }
}
