package com.example.parly;

public class message {
    String icon_message="null";
    String message;

   public   message(String icon_message, String message) {
        this.icon_message = icon_message;
        this.message = message;
    }
    public   message( String message) {
        this.icon_message = icon_message;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getIcon_message() {
        return icon_message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIcon_message(String icon_message) {
        this.icon_message = icon_message;
    }
}
