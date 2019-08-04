package com.example.parly.Notification;

public class Token {
    private String token;

    public Token(String token) {
        this.token = token;
    }

    public Token() {
    }

    public String getUser() {
        return token;
    }

    public void setUser(String token) {
        this.token = token;
    }
}
