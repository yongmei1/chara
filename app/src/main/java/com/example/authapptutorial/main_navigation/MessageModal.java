package com.example.authapptutorial.main_navigation;

public class MessageModal {

    //string t store our message and sender
    private String message;
    private String sender;

    //constructor.
    public MessageModal(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public MessageModal(String toString) {
    }

    //getter and setter methods.
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
