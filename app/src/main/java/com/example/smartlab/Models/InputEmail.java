package com.example.smartlab.Models;

public class InputEmail {
    private String email;
    private String type;

    public InputEmail(String email, String type) {
        this.email = email;
        this.type = type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

