package com.example.smartlab.Models;

public class CheckEmail {
    private String type;
    private String email;
    private String code;

    public CheckEmail(String type, String email, String code) {
        this.type = type;
        this.email = email;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
