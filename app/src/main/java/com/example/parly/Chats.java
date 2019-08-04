package com.example.parly;

public class Chats {

    private String sender;
    private  String receiver;
    private  String message;
     Chats(String sender,String receiver,String message){
        this.sender=sender;
        this.receiver=receiver;
        this.message=message;
    }
    public String getreceiver() {return receiver;}
    public  String getSender()
    { return sender;
    }

    public String getMessage() { return message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
