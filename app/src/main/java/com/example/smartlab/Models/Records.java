package com.example.smartlab.Models;

import com.google.gson.annotations.SerializedName;

public class Records {
    private int id_records;
    private String reception_time;
    private String status;
    @SerializedName("id_doctor")
    private String id_doctor;
    @SerializedName("doctors")
    private Doctors doctors;
    @SerializedName("id_client")
    private String id_client;
    @SerializedName("client")
    private Client client;

    public Records(int id_records, String reception_time, String status, String id_doctor, Doctors doctors, String id_client, Client client) {
        this.id_records = id_records;
        this.reception_time = reception_time;
        this.status = status;
        this.id_doctor = id_doctor;
        this.doctors = doctors;
        this.id_client = id_client;
        this.client = client;
    }

    public int getId_records() {
        return id_records;
    }

    public void setId_records(int id_records) {
        this.id_records = id_records;
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

    public Doctors getDoctors() {
        return doctors;
    }

    public void setDoctors(Doctors doctors) {
        this.doctors = doctors;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
