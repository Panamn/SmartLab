package com.example.smartlab.Models;

public class Order {
    private float amount;
    private String address_of_fence;
    private String id_client;
    private String phone;
    private String comment;

    public Order(float amount, String address_of_fence, String id_client, String phone, String comment) {
        this.amount = amount;
        this.address_of_fence = address_of_fence;
        this.id_client = id_client;
        this.phone = phone;
        this.comment = comment;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getAddress_of_fence() {
        return address_of_fence;
    }

    public void setAddress_of_fence(String address_of_fence) {
        this.address_of_fence = address_of_fence;
    }

    public String getId_client() {
        return id_client;
    }

    public void setId_client(String id_client) {
        this.id_client = id_client;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
