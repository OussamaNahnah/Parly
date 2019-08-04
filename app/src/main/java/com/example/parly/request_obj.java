package com.example.parly;

public class request_obj {
private String sender;
private String receiver;

    public request_obj(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }
}
