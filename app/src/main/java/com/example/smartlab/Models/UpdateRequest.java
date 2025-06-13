package com.example.smartlab.Models;


public class UpdateRequest {
    private String password;

    public UpdateRequest( String password) {

        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}