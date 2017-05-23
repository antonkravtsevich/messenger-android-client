package com.example.anton.messenger.response.chats;

/**
 * Created by anton on 22.5.17.
 */

public class Message {
    private String _id;
    private String text;
    private String date;
    private String sender_id;

    public Message(String _id, String text, String date, String sender_id) {
        this._id = _id;
        this.text = text;
        this.date = date;
        this.sender_id = sender_id;
    }

    public Message() {
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    @Override
    public String toString() {
        return "Message "+_id+" {" +
                "text='" + text + '\'' +
                ", date='" + date + '\'' +
                ", sender_id='" + sender_id + '\'' +
                '}';
    }
}

