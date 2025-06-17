package com.example.smartlab.Models;

import com.google.gson.annotations.SerializedName;

public class UpdateRecords {
    @SerializedName("reception_time")
    private String reception_time;
    @SerializedName("status")
    private String status;
    @SerializedName("id_doctor")
    private String id_doctor;
    @SerializedName("id_client")
    private String id_client;

    public UpdateRecords(String reception_time, String status, String id_doctor, String id_client) {
        this.reception_time = reception_time;
        this.status = status;
        this.id_doctor = id_doctor;
        this.id_client = id_client;
    }


    public String getReception_time() {
        return reception_time;
    }

    public void setReception_time(String reception_time) {
        this.reception_time = reception_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId_doctor() {
        return id_doctor;
    }

    public void setId_doctor(String id_doctor) {
        this.id_doctor = id_doctor;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }
}
