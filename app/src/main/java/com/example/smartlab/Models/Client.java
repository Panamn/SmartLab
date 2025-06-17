package com.example.smartlab.Models;

import com.google.gson.annotations.SerializedName;

public class Client {
    @SerializedName("id_client")
    private String id_client;
    @SerializedName("profile")
    private Profile profile;

    public Client(String id_client, Profile profile) {
        this.id_client = id_client;
        this.profile = profile;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
